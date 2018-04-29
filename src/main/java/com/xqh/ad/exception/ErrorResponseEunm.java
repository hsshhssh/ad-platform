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
    INVALID_APPID(40007, "无效appId"),
    INVALID_S2S_PARAM(40008, "s2s方式必须填写下载跳转地址"),
    DUPLICATE_LEAGUE_EN_NAME(40009, "重复联盟英文名"),
    INVALID_START_COUNT(40010, "扣量初始值不能为负数"),
    INVALID_DISCOUNT_RATE(40011, "回调率只能为[0,1],步长为0.1")
    ;



    ErrorResponseEunm(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int status;
    public String msg;
}
