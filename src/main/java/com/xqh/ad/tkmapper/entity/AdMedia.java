package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_media")
public class AdMedia
{
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 媒体名称
     */
    private String name;

    /**
     * 媒体编码
     */
    private String code;

    /**
     * 1自定义回调 2配置回调
     */
    private Integer type;

    /**
     * 回调url
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