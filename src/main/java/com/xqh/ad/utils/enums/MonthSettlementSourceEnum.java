package com.xqh.ad.utils.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * Created by hssh on 2018/4/1.
 */
@Getter
public enum MonthSettlementSourceEnum
{
    SYSTEM(1, "系统生成"),
    MANUAL(2, "手工录入"),
    ;

    private int code;
    private String name;

    MonthSettlementSourceEnum(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public static MonthSettlementSourceEnum get(Integer code) {
        if(null == code) return null;
        for (MonthSettlementSourceEnum monthSettlementSourceEnum : MonthSettlementSourceEnum.values())
        {
            if (Objects.equals(code, monthSettlementSourceEnum.getCode())) {
                return monthSettlementSourceEnum;
            }
        }
        return null;
    }
}
