package com.xqh.ad.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class AdMediaUpdateDTO
{
    @NotNull @Min(1)
    public Integer id;

    @NotBlank
    public String name;

}
