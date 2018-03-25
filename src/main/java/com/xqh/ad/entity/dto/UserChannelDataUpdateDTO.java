package com.xqh.ad.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2018/3/25.
 */
@Data
public class UserChannelDataUpdateDTO extends UserChannelDataCreateDTO
{
    @NotNull(message = "请选择需要修改的记录")
    private Integer id;
}
