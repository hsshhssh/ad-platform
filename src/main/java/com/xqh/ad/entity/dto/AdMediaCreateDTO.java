package com.xqh.ad.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class AdMediaCreateDTO
{
    @NotBlank
    private String name;

}
