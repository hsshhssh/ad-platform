package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_app")
public class AdApp {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 推广应用名称
     */
    private String name;

    /**
     * 联盟id
     */
    @Column(name = "league_id")
    private Integer leagueId;

    /**
     * 联盟编码
     */
    @Column(name = "league_code")
    private String leagueCode;

    /**
     * 联盟推广url
     */
    @Column(name = "league_url")
    private String leagueUrl;

    /**
     * 上报类型 1->302  2->s2s
     */
    @Column(name = "report_type")
    private Integer reportType;

    /**
     * 下载跳转地址 report_type=1时  league_url为上报地址和跳转地址 report_type=2是 league_url为上报地址 report_url为跳转地址
     */
    @Column(name = "redirect_url")
    private String redirectUrl;

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