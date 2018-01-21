package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_app_media")
public class AdAppMedia {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 媒体编码
     */
    @Column(name = "media_code")
    private String mediaCode;

    /**
     * url唯一标识
     */
    @Column(name = "url_code")
    private String urlCode;

    /**
     * 推广url
     */
    private String url;

    /**
     * 秘钥
     */
    @Column(name = "app_key")
    private String appKey;

    /**
     * 扣量起始值
     */
    @Column(name = "start_count")
    private Integer startCount;

    /**
     * 扣量比例 (实则为回调通过率 不要被名字误解了******)
     */
    @Column(name = "discount_rate")
    private Double discountRate;

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