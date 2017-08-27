package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_day_settlement")
public class AdDaySettlement {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 推广应用-媒体表主键
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
     * 结算日期
     */
    @Column(name = "settlement_time")
    private Integer settlementTime;

    /**
     * 点击量
     */
    @Column(name = "click_count")
    private Integer clickCount;

    /**
     * 下载量
     */
    @Column(name = "download_count")
    private Integer downloadCount;

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