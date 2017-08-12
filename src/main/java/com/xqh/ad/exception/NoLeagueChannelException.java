package com.xqh.ad.exception;

/**
 * Created by hssh on 2017/8/12.
 */
public class NoLeagueChannelException extends RuntimeException
{
    public NoLeagueChannelException() {
    }

    public NoLeagueChannelException(String message) {
        super(message);
    }

    public NoLeagueChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoLeagueChannelException(Throwable cause) {
        super(cause);
    }

    public NoLeagueChannelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
