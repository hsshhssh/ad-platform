package com.xqh.ad.entity.dto;

import lombok.Data;

/**
 * Created by hssh on 2018/5/5.
 */
@Data
public class TestReportUrlDTO
{
    /**
     * idfa
     */
    private String idfa;

    /**
     * 上报ip
     */
    private String ip;

    /**
     * 推广应用id
     */
    private Integer appMediaId;

}
