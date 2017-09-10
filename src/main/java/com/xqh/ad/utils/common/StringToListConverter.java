package com.xqh.ad.utils.common;

import com.alibaba.fastjson.JSONArray;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by hssh on 2017/6/15.
 */
@Component
public class StringToListConverter implements Converter<String, Sort>
{

    @Override
    public Sort convert(String s)
    {
        JSONArray arr = JSONArray.parseArray(s);
        return new Sort(arr.toJavaList(String.class));
    }
}
