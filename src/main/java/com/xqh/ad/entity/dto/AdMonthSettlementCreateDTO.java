package com.xqh.ad.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2018/3/31.
 */
@Data
public class AdMonthSettlementCreateDTO
{
    @NotNull(message = "请选择推广应用")
    private Integer adAppMediaId;

    @NotNull(message = "请选择投放时间")
    private Integer putTime;
}
