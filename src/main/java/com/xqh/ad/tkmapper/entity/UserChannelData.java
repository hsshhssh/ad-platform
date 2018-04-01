package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_user_channel_data")
public class UserChannelData
{
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 渠道号
     */
    @Column(name = "channel_id")
    private Integer channelId;

    /**
     * 渠道名称
     */
    @Column(name = "channel_name")
    private String channelName;

    /**
     * 推广应用名称
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * 链接id
     */
    @Column(name = "link_id")
    private String linkId;

    /**
     * 点击量
     */
    @Column(name = "click_amount")
    private Integer clickAmount;

    /**
     * 新增点击量
     */
    @Column(name = "click_increment")
    private Integer clickIncrement;

    /**
     * 新增注册
     */
    @Column(name = "register_increment")
    private Integer registerIncrement;

    /**
     * 当天充值金额
     */
    @Column(name = "recharge_amount")
    private Double rechargeAmount;

    /**
     * 人数
     */
    @Column(name = "people_amount")
    private Integer peopleAmount;

    /**
     * 统计时间
     */
    @Column(name = "statistics_date")
    private Integer statisticsDate;

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