package com.xqh.ad.service;

import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.exception.NoLeagueChannelException;
import com.xqh.ad.service.league.LeagueAbstractService;
import com.xqh.ad.service.league.ReYunAdService;
import com.xqh.ad.service.league.TencentAdService;
import com.xqh.ad.service.league.YouMengAdService;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by hssh on 2017/8/12.
 */
@Service
public class XQHAdService
{
    private static Logger logger = LoggerFactory.getLogger(XQHAdService.class);

    @Autowired
    private ReYunAdService reYunAdService;

    @Autowired
    private TencentAdService tencentAdService;

    @Autowired
    private YouMengAdService youMengAdService;


    /**
     * 根据联盟编码选择Service
     */
    public LeagueAbstractService dispatchLeague(String leagueCode)
    {
        if(Constant.REYUN.equals(leagueCode))
        {
            logger.info("热云通道");
            return reYunAdService;
        }
        else if(Constant.TENCENT.equals(leagueCode))
        {
            logger.info("腾讯通道");
            return tencentAdService;
        }
        else if(Constant.YOUMENG.equals(leagueCode))
        {
            logger.info("友盟通道");
            return youMengAdService;
        }
        else
        {
            logger.error("通道异常 leagueCode:{}",  leagueCode);
            throw new NoLeagueChannelException();
        }

    }

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
