package com.xqh.ad.entity.vo;

import lombok.Data;

/**
 * Created by hssh on 2017/8/12.
 */
@Data
public class AdAppVO
{
    private Integer id;
    private String name;
    private Integer leagueId;
    private String leagueCode;
    private String leagueUrl;
    private Integer reportType;
    private String redirectUrl;
    private Integer createTime;
    private Integer updateTime;

    private String reportTypeStr;
    private String leagueName;
}
