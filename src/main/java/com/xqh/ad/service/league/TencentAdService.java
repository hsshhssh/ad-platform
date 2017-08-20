package com.xqh.ad.service.league;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.ConfigUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/8/12.
 */
@Service
public class TencentAdService extends LeagueAbstractService
{

    @Autowired
    private ConfigUtils configUtils;

    public static Logger logger = LoggerFactory.getLogger(TencentAdService.class);

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick)
    {
        logger.info("腾讯通道 appId:{} url:{}", adApp.getId(), adApp.getLeagueUrl());

        // 上报
        try
        {
            report(adApp, adAppMedia, adClick);
        } catch (UnsupportedEncodingException e)
        {
            logger.error("腾讯通道 上报失败 appId:{} e:{}", adApp.getId(), Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;
        }


        // 跳转
        logger.info("跳转到腾讯下载地址 url:{}", adApp.getLeagueUrl());
        try
        {
            resp.sendRedirect(adApp.getLeagueUrl());
        }
        catch (IOException e) {
            logger.error("腾讯通道 跳转地址失败 appId:{} e:{}", adApp.getId(), Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;
        }
    }

    // 上报
    public void report(AdApp adApp, AdAppMedia adAppMedia, AdClick adClick) throws UnsupportedEncodingException
    {
        String host = "http://ac.o2.qq.com/php/mbclick.php";

        Map<String, String> params = Maps.newHashMap();
        params.put("sign", "report");
        params.put("gid", "107415");
        params.put("media", "9689");

        if(adClick.getPhoneType() == Constant.PHONE_TYPE_ANDROID)
        {
            params.put("imei", adClick.getImei());
        }
        else
        {

            params.put("idfa", adClick.getIdfa());
        }

        params.put("cip", adClick.getIp());
        params.put("callback", URLEncoder.encode(configUtils.getTencentCallback().trim() + "/xqh/ad/tencent/callback?clickId=" + adClick.getId(), "UTF-8"));
        params.put("time", String.valueOf(System.currentTimeMillis()/1000));
        params.put("scid", String.valueOf(adClick.getId()));
        params.put("tagid", "阳仔");
        params.put("create_id", "create_id");

        List<String> paramList = Lists.newArrayList();
        for (String s : params.keySet())
        {
            paramList.add(s.trim() + "=" + params.get(s).trim());
        }

        String url = host + "?" + Joiner.on("&").join(paramList);
        logger.info("腾讯上报地址 url：{}", url);

        HttpResult httpResult = HttpUtils.get(url);
        logger.info("腾讯上报 返回值httpResult:{}", httpResult);
    }

}
