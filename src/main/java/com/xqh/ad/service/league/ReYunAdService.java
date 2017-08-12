package com.xqh.ad.service.league;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.ConfigUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.UrlUtils;
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
public class ReYunAdService extends LeagueAbstractService
{

    @Autowired
    private ConfigUtils configUtils;

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick)
    {
        logger.info("热云推广url appid:{} url:{}", adApp.getId(), adApp.getLeagueUrl());

        try
        {
            String url = getUrl(req, resp, adApp, adAppMedia, adClick);
            logger.info("调用热云推广url:{}", url);
            resp.sendRedirect(url);
        }
        catch (IOException e)
        {
            logger.error("热云推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;
        }

    }

    public String getUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick) throws UnsupportedEncodingException {
        // 获得host 参数
        String host = UrlUtils.UrlPage(adApp.getLeagueUrl());
        Map<String, String> params = UrlUtils.URLRequest(adApp.getLeagueUrl());


        // 添加参数
        if(Constant.PHONE_TYPE_ANDROID == adClick.getPhoneType())
        {
            logger.info("热云推广 安卓 clickId:{}", adClick.getId());
            params.put("imei", adClick.getImei());
            params.put("androidid", adClick.getAndroidId());
        }
        else
        {
            logger.info("热云推广 ios clickId:{}", adClick.getId());
            params.put("idfa", adClick.getIdfa());
        }

        params.put("mac", adClick.getMac());
        params.put("ip", adClick.getIp());
        params.put("callback", URLEncoder.encode(configUtils.getHost().trim() + "/xqh/ad/reyun/callback?clickId=" + adClick.getId(), "UTF-8"));

        List<String> paramList = Lists.newArrayList();
        for (String s : params.keySet())
        {
            paramList.add(s.trim() + "=" + params.get(s).trim());
        }

        return host + "?" + Joiner.on("&").join(paramList);

    }


}
