package com.xqh.ad.utils;

/**
 * Created by hssh on 2017/8/12.
 */
public class Constant
{

    /**
     * 无效key
     */
    public final static String ERROR_CODE_KEY = "0001";

    /**
     * 无效url
     */
    public final static String ERROR_CODE_URL = "0002";


    /**
     * 未配置通道
     */
    public final static String ERROR_CHANNEL = "0003";


    /**
     * 无效的参数
     */
    public final static String ERROR_PARAM = "0004";

    /**
     * urlCode 黑名单
     */
    public final static String BLACK_LIST = "0005";


    /**
     * 无效token
     */
    public final static String ERROR_TOKEN = "0006";


    /**
     * 系统繁忙
     */
    public final static String ERROR_SYS = "1000";
    /////////////////////////////////////////////////////////////////////////

    /**
     * 热云
     */
    public final static String REYUN = "0001";

    /**
     * 腾讯
     */
    public final static String TENCENT = "0002";

    /**
     * 友盟
     */
    public final static String YOUMENG ="0003";

    /**
     * 道有道
     */
    public final static String DAOYOUDAO = "0004";

    /**
     * 瑞狮
     */
    public final static String RUISHI = "0005";

    /**
     * Cauly
     */
    public final static String CAULY = "0006";

    /**
     * 虚拟联盟
     */
    public final static String VIRTUAL = "0007";

    /////////////////////////////////////////////////////////////////////////

    /**
     * 手机类型--安卓
     */
    public final static int PHONE_TYPE_ANDROID = 1;

    /**
     * 手机类型--ios
     */
    public final static int PHONE_TYPE_IOS = 2;


    /////////////////////////////////////////////////////////////////////////


    public final static String BASCURL = "http://ad.uerbx.com";

    /**
     * 配置联盟回调地址的点击Id key
     */
    public final static String CALLBACK_CLICK_ID = "_callbackClickId";

    /**
     * 回调url
     */
    public final static String CALLBACK_URL = "_callbackUrl";

    /**
     * 时间戳
     */
    public final static String CALLBACK_TIMESTAMP = "_timestamp";


    /**
     * 固定参数
     */
    public final static String FIX_PARAM = "_fixParam";

    /**
     * 上报url clickId 的key值
     */
    public final static String REPORT_CLICK_ID = "_reportClickId";

    /**
     * 三七游戏秘钥
     */
    public final static String SIGN_37 = "_sign_37";

    /////////////////////////////////////////////////////////////////////////
    public final static String SUCC = "success";
    public final static String FAIL = "fail";


    /////////////////////////////////////////////////////////////////////////
    /**
     * mac参数常亮
     */
    public final static String MAC = "mac";

    /**
     * mac参数带上clickId的分隔符
     */
    public final static String MAC_CLICKID_SEPARATE = "-";


    //////////////////////////////////////////////////////////////////////////
    /**
     * 编译通过条件常量
     */
    public final static String CONDITION_PASS_FLAG = "pass";

}
