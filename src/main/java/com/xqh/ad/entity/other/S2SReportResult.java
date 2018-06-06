package com.xqh.ad.entity.other;

import lombok.Data;

/**
 * Created by hssh on 2017/10/15.
 */
@Data
public class S2SReportResult
{
    private Integer ret;
    private String msg;

    public S2SReportResult()
    {
    }

    public S2SReportResult(Integer ret, String msg)
    {
        this.ret = ret;
        this.msg = msg;
    }
}
