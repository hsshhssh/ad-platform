package com.xqh.ad.utils.config;

import lombok.Data;
import org.hssh.common.zkconf.Value;
import org.springframework.stereotype.Component;

/**
 * Created by hssh on 2018/1/21.
 */
@Component
@Data
public class DiscountConfigUtils
{
    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.0")
    private String _00;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.1")
    private String _10;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.2")
    private String _20;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.3")
    private String _30;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.4")
    private String _40;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.5")
    private String _50;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.6")
    private String _60;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.7")
    private String _70;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.8")
    private String _80;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "0.9")
    private String _90;

    @Value(path = "/config/zkconf/ad_discount.conf", key = "1.0")
    private String _100;


    public String getConfig(double d)
    {
        int di = (int) (d * 10);
        String config = "";
        switch (di)
        {
            case 0:
                config = this._00;
                break;
            case 1:
                config = this._10;
                break;
            case 2:
                config = this._20;
                break;
            case 3:
                config = this._30;
                break;
            case 4:
                config = this._40;
                break;
            case 5:
                config = this._50;
                break;
            case 6:
                config = this._60;
                break;
            case 7:
                config = this._70;
                break;
            case 8:
                config = this._80;
                break;
            case 9:
                config = this._90;
                break;
            case 10:
                config = this._100;
                break;
        }

        return config.trim();
    }
}
