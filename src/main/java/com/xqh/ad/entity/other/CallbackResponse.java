package com.xqh.ad.entity.other;

import com.xqh.ad.utils.constant.CallbackResponseEnum;
import lombok.Data;

/**
 * Created by hssh on 2017/10/21.
 */
@Data
public class CallbackResponse
{
    private String status;
    private String message;

    public CallbackResponse()
    {
    }

    public CallbackResponse(String status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public CallbackResponse(CallbackResponseEnum callbackResponseEnum)
    {
        this.status = callbackResponseEnum.getStatus();
        this.message = callbackResponseEnum.getMessage();
    }
}
