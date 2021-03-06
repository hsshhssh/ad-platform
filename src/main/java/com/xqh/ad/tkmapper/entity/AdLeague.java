package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_league")
public class AdLeague {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 联盟名称
     */
    private String name;

    /**
     * 联盟编码
     */
    private String code;

    /**
     * 联盟英文名
     */
    @Column(name = "en_name")
    private String enName;

    /**
     * 联盟回调地址
     */
    @Column(name = "callback_url")
    private String callbackUrl;

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