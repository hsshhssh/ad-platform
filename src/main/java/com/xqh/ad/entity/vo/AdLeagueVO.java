package com.xqh.ad.entity.vo;

import lombok.Data;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class AdLeagueVO
{
    private Integer id;
    private String name;
    private String code;
    private String enName;
    private String callbackUrl;
    private Integer createTime;
    private Integer updateTime;
}
