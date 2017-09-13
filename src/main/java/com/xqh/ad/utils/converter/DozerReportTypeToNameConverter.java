package com.xqh.ad.utils.converter;

import com.xqh.ad.utils.constant.ReportTypeEnum;
import org.dozer.CustomConverter;

/**
 * Created by hssh on 2017/9/13.
 */
public class DozerReportTypeToNameConverter implements CustomConverter
{
    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass)
    {
        for (ReportTypeEnum anEnum : ReportTypeEnum.values())
        {
            if(anEnum.getValue() == (int)(sourceFieldValue))
                return anEnum.getName();
        }

        return null;
    }
}
