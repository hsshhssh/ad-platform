package com.xqh.ad.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class AdAppMediaUpdateDTO
{
    @NotNull @Min(1)
    private Integer id;

    private Integer startCount;

    private Double discountRate;

    @NotBlank
    private String appKey;

}
