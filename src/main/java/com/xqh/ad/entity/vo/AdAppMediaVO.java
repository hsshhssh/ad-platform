package com.xqh.ad.entity.vo;

import lombok.Data;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class AdAppMediaVO
{
    private Integer id;
    private Integer appId;
    private Integer mediaId;
    private String mediaCode;
    private String urlCode;
    private String url;
    private String appKey;
    private Integer startCount;
    private Double discountRate;
    private Integer createTime;
    private Integer updateTime;

    private String appName;
    private String mediaName;
}
