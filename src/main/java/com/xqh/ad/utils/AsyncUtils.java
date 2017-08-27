package com.xqh.ad.utils;

import com.xqh.ad.entity.other.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by hssh on 2017/8/25.
 */
@Component
public class AsyncUtils
{
    public static Logger logger = LoggerFactory.getLogger(AsyncUtils.class);

    /**
     * 回调
     */
    @Async
    public void callbackUser(String callback)
    {
        logger.info("异步回调开始 callback:{}");
        String url = null;
        try
        {
            url = URLDecoder.decode(callback, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("异步回到异常 callback:{}", callback);
            return ;
        }

        logger.info("异步回调url:{}", url);

        HttpResult httpResult = HttpUtils.get(url);

        logger.info("异步回调结果 httpResult:{}", httpResult);

    }

}
