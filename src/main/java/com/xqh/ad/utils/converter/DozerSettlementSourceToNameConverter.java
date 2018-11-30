package com.xqh.ad.utils.converter;

import com.xqh.ad.utils.enums.MonthSettlementSourceEnum;
import org.dozer.CustomConverter;

/**
 * Created by hssh on 2018/4/1.
 */
public class DozerSettlementSourceToNameConverter implements CustomConverter
{
    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass)
    {
        Integer sourceCode = (Integer) sourceFieldValue;
        MonthSettlementSourceEnum sourceEnum = MonthSettlementSourceEnum.get(sourceCode);
        return null == sourceEnum ? "" : sourceEnum.getName();
    }
}
