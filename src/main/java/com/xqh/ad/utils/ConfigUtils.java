package com.xqh.ad.utils;

import lombok.Data;
import org.hssh.common.zkconf.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hssh on 2017/8/13.
 */
@Component
@Data
public class ConfigUtils
{
    @Value(path = "/config/zkconf/ad", key = "host")
    private String host;

    @Value(path = "/config/zkconf/ad", key = "tencentCallback")
    private String tencentCallback;

    /**
     * 0不带参数回调 否则待参数回调
     */
    @Value(path = "/config/zkconf/ad", key = "tencentCallbackParamSwitch")
    private String tencentCallbackParamSwitch;

    @Value(path = "/config/zkconf/ad", key = "urlCodeBlackList")
    private String urlCodeBlackList;

    @Value(path = "/config/zkconf/ad", key = "defaultStartCount")
    private String defaultStartCount;

    @Value(path = "/config/zkconf/ad", key = "defaultDiscountRate")
    private String defaultDiscountRate;

    @Value(path = "/config/zkconf/ad", key = "ppMediaCode")
    private String ppMediaCode;

    @Value(path = "/config/zkconf/ad", key = "macWithClickIdLeague")
    private List<String> macWithClickIdLeague;


}
