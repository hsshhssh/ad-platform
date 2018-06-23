package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_download_count")
public class AdDownloadCount {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 推广应用id
     */
    @Column(name = "app_media_id")
    private Integer appMediaId;

    /**
     * 下载总数
     */
    @Column(name = "download_count")
    private Integer downloadCount;
}