package com.xqh.ad.utils.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.hssh.common.zkconf.ValueWithMethod;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hssh on 2018/3/20.
 */
@Component
@Slf4j
public class ClickExtendParamsConfigUtils
{
    private Map<Integer, String> params1Map = Maps.newConcurrentMap();

    @ValueWithMethod(path = "/config/zkconf/ad_click_extend_params1.conf")
    public void ppMediaParamHandler(PropertiesConfiguration properties) {
        Map<Integer, String> tempMap = Maps.newHashMap();
        Iterator<String> keys = properties.getKeys();
        String key;
        while (keys.hasNext())
        {
            key = keys.next().trim();
            tempMap.put(Integer.valueOf(key), properties.getString(key).trim());
        }
        params1Map = ImmutableMap.copyOf(tempMap);
        log.info("点击自定义参数1 param1Map:{}", params1Map);
    }

    public String getExtendParams1(Integer appMediaId) {
        return params1Map.get(appMediaId);
    }

}
