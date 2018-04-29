package com.xqh.ad.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class AdAppMediaCreateDTO
{
    @NotNull @Min(1)
    private Integer appId;

    @NotNull @Min(1)
    private Integer mediaId;

    @NotBlank
    private String appKey;

    /**
     * 扣量初始值
     */
    private Integer startCount;

    /**
     * 回调率
     */
    private Double discountRate;

}
