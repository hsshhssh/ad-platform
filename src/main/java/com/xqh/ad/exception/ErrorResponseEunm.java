package com.xqh.ad.exception;

/**
 * Created by hssh on 2017/6/4.
 */
public enum ErrorResponseEunm
{
    INVALID_PASSWORD(40000, "密码错误"),
    INVALID_USER(40001, "用户不存在"),
    DUPLICATE_USERNAME(40002, "用户名重复"),
    INVALID_METHOD_ARGS(40003, "参数校验失败"),

    INVALID_APPID_MEDIAID(40004, "无效appId或mediaId"),
    DUPLICATE_URLCODE(40005, "该应用和媒体已存在推广链接"),
    INVALID_LEAGUEID(40006, "无效leagueId"),
    ;



    ErrorResponseEunm(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int status;
    public String msg;
}
