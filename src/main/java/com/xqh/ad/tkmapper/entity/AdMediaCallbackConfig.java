package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_media_callback_config")
public class AdMediaCallbackConfig {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 媒体id
     */
    @Column(name = "media_id")
    private Integer mediaId;

    /**
     * 媒体回调key
     */
    @Column(name = "media_key")
    private String mediaKey;

    /**
     * 新企航回调key
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