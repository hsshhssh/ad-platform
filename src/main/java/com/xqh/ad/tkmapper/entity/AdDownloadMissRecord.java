package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_download_miss_record")
public class AdDownloadMissRecord {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 丢失的点击记录id
     */
    @Column(name = "miss_click_id")
    private Integer missClickId;

    /**
     * 媒体回调url
     */
    @Column(name = "media_callback_url")
    private String mediaCallbackUrl;

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