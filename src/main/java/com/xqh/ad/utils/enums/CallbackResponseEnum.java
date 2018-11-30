package com.xqh.ad.utils.enums;

import com.xqh.ad.utils.Constant;

/**
 * Created by hssh on 2017/10/21.
 */
public enum CallbackResponseEnum
{
    ERROR_CLICK_ID(Constant.FAIL, "error clickId"),
    SUCC(Constant.SUCC, "callback success"),
    ;

    private String status;
    private String message;

    CallbackResponseEnum(String status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
