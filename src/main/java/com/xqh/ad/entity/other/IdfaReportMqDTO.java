package com.xqh.ad.entity.other;

import lombok.Data;

/**
 * Created by hssh on 2018/6/4.
 */
@Data
public class IdfaReportMqDTO
{

    /**
     * 上报记录id
     */
    private Integer id;

    /**
     * 推广应用id
     */
    private Integer appMediaId;

    /**
     * ip
     */
    private String ip;

    /**
     * idfa
     */
    private String idfa;

    /**
     * 上报表下表
     */
    private String tableIndex;

}
