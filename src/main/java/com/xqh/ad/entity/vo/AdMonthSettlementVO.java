package com.xqh.ad.entity.vo;

import com.xqh.ad.tkmapper.entity.AdMonthSettlement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by hssh on 2018/3/31.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AdMonthSettlementVO extends AdMonthSettlement
{

    private String sourceName;
    private String mediaName;
    private String leagueName;
    private String appName;

}
