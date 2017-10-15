package com.xqh.ad.entity.other;

import lombok.Data;

/**
 * Created by hssh on 2017/10/15.
 */
@Data
public class S2SReportResult
{
    private String status;
    private String token;

    public S2SReportResult()
    {
    }

    public S2SReportResult(String status, String token)
    {
        this.status = status;
        this.token = token;
    }
}
