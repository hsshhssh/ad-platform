package com.xqh.ad.utils.enums;

/**
 * Created by hssh on 2017/10/1.
 */
public enum MediaTypeEnum
{
    CUSTOM_CALLBACK(1, "自定义回调"),
    CONFIG_CALLBACK(2, "配置回调"),
    ;


    private int value;
    private String name;

    MediaTypeEnum(int value, String name)
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
}
