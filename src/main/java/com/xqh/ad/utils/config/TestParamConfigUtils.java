package com.xqh.ad.utils.config;

import lombok.Data;
import org.hssh.common.zkconf.Value;
import org.springframework.stereotype.Component;

/**
 * Created by hssh on 2017/9/11.
 */
@Component
@Data
public class TestParamConfigUtils
{

    @Value(path = "/config/zkconf/ad_test_param.conf", key = "host")
    private String host;

    @Value(path = "/config/zkconf/ad_test_param.conf", key = "ip")
    private String ip;

    @Value(path = "/config/zkconf/ad_test_param.conf", key = "idfa")
    private String idfa;

    @Value(path = "/config/zkconf/ad_test_param.conf", key = "callback")
    private String callback;

}
