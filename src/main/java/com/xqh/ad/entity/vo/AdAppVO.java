package com.xqh.ad.entity.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by hssh on 2017/8/12.
 */
@Data
public class AdAppVO
{
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
