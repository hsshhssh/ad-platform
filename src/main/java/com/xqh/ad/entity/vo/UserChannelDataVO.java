package com.xqh.ad.entity.vo;

import lombok.Data;

/**
 * Created by hssh on 2018/3/25.
 */
@Data
public class UserChannelDataVO
{
    private Integer id;

    private Integer userId;

    private Integer channelId;

    private String channelName;

    private String appName;

    private String linkId;

    private Integer clickAmount;

    private Integer clickIncrement;

    private Integer registerIncrement;

    private Double rechargeAmount;

    private Integer peopleAmount;

    private Integer statisticsDate;
}
