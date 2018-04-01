package com.xqh.ad.entity.vo;

import com.xqh.ad.tkmapper.entity.AdMonthSettlement;
import lombok.Data;

/**
 * Created by hssh on 2018/3/31.
 */
@Data
public class AdMonthSettlementVO extends AdMonthSettlement
{

    private String sourceName;
    private String mediaName;
    private String leagueName;
    private String appName;

}
