package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_ruishi_info")
public class AdRuishiInfo
{
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
     * 瑞狮信息q字段
     */
    @Column(name = "ruishi_q")
    private String ruishiQ;

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