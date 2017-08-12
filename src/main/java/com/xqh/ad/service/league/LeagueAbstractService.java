package com.xqh.ad.service.league;

import com.sun.tools.doclets.formats.html.AllClassesFrameWriter;
import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.exception.RequestParamException;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by hssh on 2017/8/12.
 */
public abstract class LeagueAbstractService
{

    public static Logger logger = LoggerFactory.getLogger(LeagueAbstractService.class);

    @Autowired
    private AdClickMapper adClickMapper;

    /**
     * 获取点击点击实体类
     */
    public AdClick insertClickRecord(HttpServletRequest req, AdAppMedia adAppMedia, AdApp adApp)
    {
        AdClick adClick = new AdClick();

        adClick.setAppMediaId(adAppMedia.getId());
        adClick.setAppId(adApp.getId());
        adClick.setMediaId(adAppMedia.getMediaId());
        adClick.setLeagueId(adApp.getLeagueId());

        String imei = req.getParameter("imei");
        String mac = req.getParameter("mac");
        String ip = req.getParameter("ip");
        String androidId = req.getParameter("androidId");
        String idfa = req.getParameter("idfa");
        String callback = req.getParameter("callback");

        if(StringUtils.isBlank(imei) || StringUtils.isBlank(mac) || StringUtils.isBlank(ip))
        {
            logger.error("请求参数异常 imei:{}  mac:{}  ip:{}", imei, mac, ip);
            throw new RequestParamException("imei mac ip异常");
        }

        if(null != callback && callback.length() > 500)
        {
            logger.error("请求参数 回调url参数过长 callback:{}",callback);
        }

        adClick.setImei(imei);
        adClick.setMac(mac);
        adClick.setIp(ip);
        adClick.setAndroidId(androidId);
        adClick.setIdfa(idfa);
        adClick.setPhoneType(getPhoneType(androidId, idfa));
        adClick.setCallbackUrl(callback);

        int nowTime = (int) (System.currentTimeMillis()/1000);
        adClick.setCreateTime(nowTime);
        adClick.setUpdateTime(nowTime);

        adClickMapper.insertSelective(adClick);

        return adClick;

    }

    /**
     * 获取手机类型
     */
    private int getPhoneType(String androidId, String idfa)
    {
        if(StringUtils.isNotBlank(androidId))
        {
            logger.info("手机类型--安卓");
            return Constant.PHONE_TYPE_ANDROID;
        }
        else if(StringUtils.isNotBlank(idfa))
        {
            logger.info("手机类型--ios");
            return Constant.PHONE_TYPE_IOS;
        }
        else
        {
            logger.error("手机类型--异常 idfa:{} && androidnId:{} 空 ", idfa, androidId);
            throw new RequestParamException("手机类型异常");
        }
    }


    /**
     * 跳转到推广url
     */
    public abstract void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick);


}
