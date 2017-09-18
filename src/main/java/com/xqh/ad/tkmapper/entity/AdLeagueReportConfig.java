package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_league_report_config")
public class AdLeagueReportConfig
{
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 联盟id
     */
    @Column(name = "league_id")
    private Integer leagueId;

    /**
     * 联盟上报key
     */
    @Column(name = "league_key")
    private String leagueKey;

    /**
     * 新企航上报key
     */
    @Column(name = "xqh_key")
    private String xqhKey;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Integer updateTime;
}