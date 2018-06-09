package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_click_history")
public class AdClickHistory {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 推广应用-媒体主键
     */
    @Column(name = "app_media_id")
    private Integer appMediaId;

    /**
     * 推广应用id
     */
    @Column(name = "app_id")
    private Integer appId;

    /**
     * 媒体id
     */
    @Column(name = "media_id")
    private Integer mediaId;

    /**
     * 联盟id
     */
    @Column(name = "league_id")
    private Integer leagueId;

    /**
     * 手机类型 1安卓 2苹果
     */
    @Column(name = "phone_type")
    private Integer phoneType;

    /**
     * 手机标识imei
     */
    private String imei;

    /**
     * 物理地址
     */
    private String mac;

    /**
     * ip
     */
    private String ip;

    /**
     * 手机标识android_id
     */
    @Column(name = "android_id")
    private String androidId;

    /**
     * 手机标识idfa
     */
    private String idfa;

    /**
     * 回调url
     */
    @Column(name = "callback_url")
    private String callbackUrl;

    /**
     * 扩展参数1
     */
    @Column(name = "extend_params_1")
    private String extendParams1;

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