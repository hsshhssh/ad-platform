package com.xqh.ad.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class AdAppCreateDTO
{
    @NotBlank
    private String name;

    @NotNull @Min(1)
    private Integer leagueId;

    @NotBlank
    private String leagueUrl;

    @NotNull @Range(min = 1, max = 2)
    private Integer reportType;

    private String redirectUrl;

}
