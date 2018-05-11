package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_freeload_relate")
public class AdFreeloadRelate {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 被蹭量推广应用id
     */
    @Column(name = "source_app_media_id")
    private Integer sourceAppMediaId;

    /**
     * 蹭量推广应用id
     */
    @Column(name = "dest_app_media_id")
    private Integer destAppMediaId;

    /**
     * 备注
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