package com.xqh.ad.service.league;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/8/31.
 */
@Service
public class DaoYouDaoAdService extends LeagueAbstractService
{
    private static Logger logger = LoggerFactory.getLogger(DaoYouDaoAdService.class);

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick, String reportTypeParam)
    {
        logger.info("道有道推广url appId:{} url:{}", adApp.getId(), adApp.getLeagueUrl());

        try
        {
            String url = getUrl(adApp, adClick);
            logger.info("调用道有道推广url:{}", url);
            resp.sendRedirect(url);
        }
        catch (IOException e)
        {
            logger.error("道有道推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;
        }

    }


    public String getUrl(AdApp adApp, AdClick adClick)
    {
        // 获得域名
        String host = UrlUtils.UrlPage(adApp.getLeagueUrl());
        Map<String, String> params = UrlUtils.URLRequest(adApp.getLeagueUrl());

        // 添加参数
        if(Constant.PHONE_TYPE_ANDROID == adClick.getPhoneType())
        {
            logger.info("道有道推广 安卓 clickId:{}", adClick.getId());
            params.put("imei", adClick.getImei());
            params.put("androidId", adClick.getAndroidId());
            //TODO gaid 安卓设备 Google ad Id
        }
        else
        {
            logger.info("道有道推广 ios clickId:{}", adClick.getId());
            params.put("idfa", adClick.getIdfa());
        }

        params.put("clickid", String.valueOf(adClick.getId()));
        //params.put("mac", adClick.getMac());

        List<String> paramList = Lists.newArrayList();
        for (String s : params.keySet())
        {
            if(StringUtils.isNotBlank(params.get(s)))
            {
                paramList.add(s.trim() + "=" + params.get(s).trim());
            }
        }

        return host + "?" + Joiner.on("&").join(paramList);

    }
}
