package com.xqh.account.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class XqhUserCreateDTO
{
    @NotNull
    private String username;
}
