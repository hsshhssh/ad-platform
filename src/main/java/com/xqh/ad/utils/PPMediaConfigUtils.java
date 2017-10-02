package com.xqh.ad.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.xqh.ad.entity.other.PPMediaParam;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.hssh.common.zkconf.ValueWithMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/10/2.
 */
@Component
public class PPMediaConfigUtils
{
    private static Logger logger = LoggerFactory.getLogger(PPMediaConfigUtils.class);

    public static Map<Integer, PPMediaParam> paramMap = Maps.newHashMap();

    @ValueWithMethod(path = "/config/zkconf/ad_pp_media.conf")
    public void ppMediaParamHandler(PropertiesConfiguration properties)
    {
        try
        {
            Map<Integer, PPMediaParam> tempMap = Maps.newHashMap();
            Iterator<String> keys = properties.getKeys();
            String key;
            while (keys.hasNext())
            {
                key = keys.next();
                List<String> list = Splitter.on("_").trimResults().splitToList(key);

                if(null == tempMap.get(Integer.valueOf(list.get(0))))
                {
                    PPMediaParam tempParam = new PPMediaParam();
                    tempMap.put(Integer.valueOf(list.get(0)), tempParam);
                }

                Field field = PPMediaParam.class.getDeclaredField(list.get(1));
                field.setAccessible(true);
                field.set(tempMap.get(Integer.valueOf(list.get(0))), properties.getString(key));
            }

            paramMap = ImmutableMap.copyOf(tempMap);
            logger.info("pp媒体参数 paramMap:{}", paramMap);
        } catch (NoSuchFieldException e)
        {
            logger.error("pp助手配置异常 e:{}", Throwables.getStackTraceAsString(e));
        } catch (IllegalAccessException e)
        {
            logger.error("pp助手配置异常 e:{}", Throwables.getStackTraceAsString(e));
        }
    }


}
