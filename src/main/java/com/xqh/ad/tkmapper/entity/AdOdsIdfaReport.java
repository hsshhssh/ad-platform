package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_ods_idfa_report")
public class AdOdsIdfaReport {
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
     * ip
     */
    private String ip;

    /**
     * 手机标识idfa
     */
    private String idfa;

    /**
     * 状态 0-初始 1-已发送消息队列
     */
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;
}