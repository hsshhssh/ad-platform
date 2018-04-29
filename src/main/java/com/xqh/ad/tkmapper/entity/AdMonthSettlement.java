package com.xqh.ad.tkmapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ad_month_settlement")
public class AdMonthSettlement {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    /**
     * 来源 1-系统生成 2-手工录入
     */
    protected Integer source;

    /**
     * 投放时间
     */
    @Column(name = "put_time")
    protected Integer putTime;

    /**
     * 媒体id
     */
    @Column(name = "media_id")
    protected Integer mediaId;

    /**
     * 联盟id
     */
    @Column(name = "league_id")
    protected Integer leagueId;

    /**
     * 应用id
     */
    @Column(name = "app_id")
    protected Integer appId;

    /**
     * 总下载量
     */
    @Column(name = "total_download")
    protected Integer totalDownload;

    /**
     * 扣量数
     */
    @Column(name = "skip_download")
    protected Integer skipDownload;

    /**
     * 回调下载数
     */
    @Column(name = "callback_download")
    protected Integer callbackDownload;

    /**
     * 结算时间
     */
    @Column(name = "settlement_time")
    protected Integer settlementTime;

    /**
     * 结算扣量数
     */
    @Column(name = "settlement_skip")
    protected Integer settlementSkip;

    /**
     * 结算回调数
     */
    @Column(name = "settlement_callback")
    protected Integer settlementCallback;

    /**
     * 上游单价
     */
    @Column(name = "upstream_price")
    protected Double upstreamPrice;

    /**
     * 下游单价数
     */
    @Column(name = "downstream_price")
    protected Double downstreamPrice;

    /**
     * 差价利润
     */
    @Column(name = "diff_profit")
    protected Double diffProfit;

    /**
     * 扣量利润
     */
    @Column(name = "skip_profit")
    protected Double skipProfit;

    /**
     * 总利润
     */
    @Column(name = "total_profit")
    protected Double totalProfit;

    /**
     * 已确认 0-未确认 1-已确认
     */
    @Column(name = "is_confirm")
    protected Integer isConfirm;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    protected Integer createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    protected Integer updateTime;
}