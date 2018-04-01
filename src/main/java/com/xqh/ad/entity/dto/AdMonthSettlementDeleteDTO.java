package com.xqh.ad.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2018/4/1.
 */
@Data
public class AdMonthSettlementDeleteDTO
{
    @NotNull(message = "请选择要删除的记录")
    private Integer id;

}
