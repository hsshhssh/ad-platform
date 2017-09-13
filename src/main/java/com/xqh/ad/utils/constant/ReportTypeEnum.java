package com.xqh.ad.utils.constant;

/**
 * Created by hssh on 2017/9/13.
 */
public enum ReportTypeEnum
{
    _302(1, "302"),
    _S2S(2, "s2s"),
    ;

    ReportTypeEnum(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private int value;
    private String name;

}
