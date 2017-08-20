package com.xqh.ad.utils;

import lombok.Data;
import org.hssh.common.zkconf.Value;
import org.springframework.stereotype.Component;

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


}
