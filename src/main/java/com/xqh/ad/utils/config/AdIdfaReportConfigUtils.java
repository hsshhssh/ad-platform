package com.xqh.ad.utils.config;

import com.google.common.base.Splitter;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.hssh.common.zkconf.ValueWithMethod;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class AdIdfaReportConfigUtils {

    private ConcurrentMap<String, Table<Integer, Integer, Integer>> tableMinMap = Maps.newConcurrentMap();
    private ConcurrentMap<String, Integer> defaultCountMap = Maps.newConcurrentMap();

    private Table<Integer, Integer, Integer> rawTable = HashBasedTable.create();

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf")
    public void adIdfaReportConfigHandle(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        rawTable = initTable("-1", properties);
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf0")
    public void adIdfaReportConfigHandle0(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("0", initTable("0", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf1")
    public void adIdfaReportConfigHandle1(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("1", initTable("1", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf2")
    public void adIdfaReportConfigHandle2(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("2", initTable("2", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf3")
    public void adIdfaReportConfigHandle3(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("3", initTable("3", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf4")
    public void adIdfaReportConfigHandle4(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("4", initTable("4", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf5")
    public void adIdfaReportConfigHandle5(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("5", initTable("5", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf6")
    public void adIdfaReportConfigHandle6(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("6", initTable("6", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf7")
    public void adIdfaReportConfigHandle7(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("7", initTable("7", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf8")
    public void adIdfaReportConfigHandle8(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("8", initTable("8", properties));
    }

    @ValueWithMethod(path = "/config/zkconf/ad_idfa_report.conf9")
    public void adIdfaReportConfigHandle9(ByteArrayInputStream inputStream) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.load(inputStream, "utf-8");
        tableMinMap.put("9", initTable("9", properties));
    }


    public Integer getMinValue(Integer hour, Integer min)
    {
        Integer value = rawTable.get(hour, min);
        if(null == value)
        {
            log.error("ad_idfa_report配置代码有误 defaultCount:{}", defaultCountMap.get("-1"));
            return defaultCountMap.get("-1");
        }
        else
        {
            return value;
        }
    }

    public Integer getMinValue(String tableIndex, Integer hour, Integer min)
    {
        Integer value = tableMinMap.get(tableIndex).get(hour, min);
        if(null == value)
        {
            log.error("ad_idfa_report配置代码有误 defaultCount:{}", defaultCountMap.get(tableIndex));
            return defaultCountMap.get(tableIndex);
        }
        else
        {
            return value;
        }
    }

    private Table<Integer, Integer, Integer> initTable(String tableIndex, PropertiesConfiguration properties)
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
                defaultCountMap.put(tableIndex, Integer.valueOf(properties.getString(key)));
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
        fillTempMinTable(tableIndex, tempMinTable, tempHourMap);

        printTable(tempMinTable);

        return tempMinTable;
    }

    /**
     * 打印table
     */
    private void printTable(Table<Integer, Integer, Integer> table)
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
    private void fillTempMinTable(String tableIndex, Table<Integer, Integer, Integer> tempMinTable, Map<Integer, Integer> tempHourMap)
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
                        value = defaultCountMap.get(tableIndex);
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
