package com.xqh.ad.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2018/3/31.
 */
@Data
public class AdMonthSettlementUpdateDTO
{
    @NotNull(message = "请选择要更新的记录")
    private Integer id;

    @NotNull(message = "请选择结算时间")
    private Integer settlementTime;

    @NotNull(message = "请填写结算扣量数")
    private Integer settlementSkip;

    @NotNull(message = "请填写结算回调数")
    private Integer settlementCallback;

    @NotNull(message = "请填写上游单价")
    private Double upstreamPrice;

    @NotNull(message = "请填写下游单价")
    private Double downstreamPrice;

}
