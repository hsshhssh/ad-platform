package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_tencent_info")
public class AdTencentInfo {
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
     * 腾讯mediaId
     */
    @Column(name = "tencent_media_id")
    private Integer tencentMediaId;

    /**
     * 腾讯投放游戏唯一标识
     */
    @Column(name = "tencent_game_id")
    private Integer tencentGameId;

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