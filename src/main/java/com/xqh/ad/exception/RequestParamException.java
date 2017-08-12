package com.xqh.ad.exception;

/**
 * Created by hssh on 2017/8/13.
 */
public class RequestParamException extends RuntimeException
{
    public RequestParamException() {
    }

    public RequestParamException(String message) {
        super(message);
    }

    public RequestParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestParamException(Throwable cause) {
        super(cause);
    }

    public RequestParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
