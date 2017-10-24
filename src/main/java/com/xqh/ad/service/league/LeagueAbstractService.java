package com.xqh.ad.service.league;

import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.entity.other.S2SReportResult;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        //if(StringUtils.isBlank(imei) || StringUtils.isBlank(mac) || StringUtils.isBlank(ip))
        //if(StringUtils.isBlank(ip))
        //{
        //    logger.error("请求参数异常 imei:{}  mac:{}  ip:{}", imei, mac, ip);
        //    throw new RequestParamException("ip异常");
        //}

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
        //else if(StringUtils.isNotBlank(idfa))
        // 默认是ios
        else
        {
            logger.info("手机类型--ios");
            return Constant.PHONE_TYPE_IOS;
        }
        //else
        //{
        //    logger.error("手机类型--异常 idfa:{} && androidnId:{} 空 ", idfa, androidId);
        //    throw new RequestParamException("手机类型异常");
        //}
    }


    /**
     * 跳转到推广url
     */
    public abstract void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick, String reportTypeParam);


    public void dealWithS2S(String reportTypeParam, HttpServletResponse resp, AdClick adClick, AdApp adApp)
    {
        String reportTypeStr = reportTypeParam;
        if(null != reportTypeStr && 2 == Integer.valueOf(reportTypeStr))
        {
            logger.info("下游使用s2s方式 上报上游 返回成功");
            S2SReportResult reportResult = new S2SReportResult("success", String.valueOf(adClick.getId()));
            CommonUtils.writeResponse(resp, JSONObject.toJSONString(reportResult));
            return;
        }
        else
        {
            logger.info("下游使用302方式 上报上游 跳转地址");
            String redirectUrl = adApp.getRedirectUrl();
            try
            {
                resp.sendRedirect(redirectUrl);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return ;
        }
    }

}
