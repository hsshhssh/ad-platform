package com.xqh.ad.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by hssh on 2018/3/25.
 */
@Data
public class UserChannelDataCreateDTO
{
    @NotNull(message = "请选择渠道")
    protected Integer channelId;

    @NotBlank(message = "请填写游戏名称")
    @Length(max = 50)
    protected String appName;

    @NotBlank(message = "请填写链接id")
    @Length(max = 50)
    protected String linkId;

    @NotNull(message = "请填写点击量")
    protected Integer clickAmount;

    @NotNull(message = "请填写新增点击量")
    protected Integer clickIncrement;

    @NotNull(message = "请填写新增注册量")
    protected Integer registerIncrement;

    @NotNull(message = "请填写当天总充值")
    protected BigDecimal rechargeAmount;

    @NotNull(message = "请填写人数")
    protected Integer peopleAmount;

    @NotNull(message = "请选择统计日期")
    protected Integer statisticsDate;

}
