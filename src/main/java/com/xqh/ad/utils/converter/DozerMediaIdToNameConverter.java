package com.xqh.ad.utils.converter;

import com.xqh.ad.utils.CacheUtils;
import org.dozer.CustomConverter;

/**
 * Created by hssh on 2017/9/10.
 */
public class DozerMediaIdToNameConverter implements CustomConverter
{
    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass)
    {
        return CacheUtils.getMediaNameById((Integer) sourceFieldValue);
    }
}
