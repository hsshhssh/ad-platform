package com.xqh.ad.utils.config;

import com.google.common.base.Splitter;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.hssh.common.zkconf.ValueWithMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AdIdfaReportConfigUtils {

    private ImmutableTable<Integer, Integer, Integer> minTable;

    private volatile Integer defaultCount = 200;

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf")
    public void adIdfaReportConfigHandle(PropertiesConfiguration properties)
    {

        Iterator<String> keys = properties.getKeys();
        String key;
        Map<Integer, Integer> tempHourMap = new HashMap<>();
        Table<Integer, Integer, Integer> tempMinTable = HashBasedTable.create();
        while (keys.hasNext())
        {
            key = keys.next();
            // 特殊化处理
            if("defaultCount".equals(key))
            {
                defaultCount = Integer.valueOf(properties.getString(key));
                continue;
            }


            List<String> keyList = convertKeyToList(key);
            if(keyList.size() == 2)
            {
                // 小时
                tempHourMap.put(getHour(keyList), Integer.valueOf(properties.getString(key)));
            }
            else if (keyList.size() == 4)
            {
                // 分钟
                Integer hour = getHour(keyList);
                List<Integer> minList = getMinList(keyList);
                Integer minValue = Integer.valueOf(properties.getString(key));
                for (Integer min : minList)
                {
                    tempMinTable.put(hour, min, minValue);
                }
            }
        }

        // 填充缺失配置
        fillTempMinTable(tempMinTable, tempHourMap);

        minTable = ImmutableTable.copyOf(tempMinTable);
        printTable(minTable);
    }

    public Integer getMinValue(Integer hour, Integer min)
    {
        Integer value = minTable.get(hour, min);
        if(null == value)
        {
            log.error("ad_idfa_report配置代码有误 defaultCount:{}", defaultCount);
            return defaultCount;
        }
        else
        {
            return value;
        }
    }


    /**
     * 打印table
     */
    public void printTable(Table<Integer, Integer, Integer> table)
    {
        log.info("=============print table begin=============");
        StringBuilder sb = new StringBuilder();
        for (Integer row : table.rowKeySet()) {
            Map<Integer, Integer> columnMap = table.row(row);
            for (Integer column : columnMap.keySet()) {
                sb.append(row);
                sb.append("_" + column);
                sb.append("=" + columnMap.get(column));
                sb.append(" ");
            }
            sb.append("\n");
        }
        log.info("{}", sb.toString());
        log.info("=============print table finish=============");
    }


    /**
     * 填充缺失配置
     */
    private void fillTempMinTable(Table<Integer, Integer, Integer> tempMinTable, Map<Integer, Integer> tempHourMap)
    {
        for (int h=0; h<24; h++)
        {
            for (int m=0; m<60; m++)
            {
                if(tempMinTable.get(h, m) == null)
                {
                    Integer value;
                    // 先从小时配置中找
                    Integer hourValue = tempHourMap.get(h);
                    if(null != hourValue)
                    {
                        value = hourValue;
                    }
                    else
                    {
                        value = defaultCount;
                    }
                    tempMinTable.put(h, m, value);
                }
            }
        }
    }

    private List<String> convertKeyToList(String key)
    {
        return Splitter.on("_").trimResults().omitEmptyStrings().splitToList(key);
    }

    private Integer getHour(List<String> hourKeyList)
    {
        String s = hourKeyList.get(1);
        return Integer.valueOf(s);
    }

    private List<Integer> getMinList(List<String> minKeyList)
    {
        Integer start = Integer.valueOf(minKeyList.get(2));
        Integer end = Integer.valueOf(minKeyList.get(3));
        List<Integer> list = Lists.newArrayList();
        for(int i=start; i<=end; i++)
        {
            list.add(i);
        }
        return list;
    }


}
