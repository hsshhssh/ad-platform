package com.xqh.ad.service.league;

import com.google.common.base.Throwables;
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
import java.util.Map;

/**
 * Created by hssh on 2017/9/11.
 */
@Service
public class CaulyAdService extends LeagueAbstractService
{
    private static Logger logger = LoggerFactory.getLogger(CaulyAdService.class);

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick)
    {
        logger.info("Cauly推广url appid:{} url:{}", adApp.getId(), adApp.getLeagueUrl());


        try
        {
            String url = getUrl(adApp, adClick);

            if(StringUtils.isBlank(url))
            {
                logger.info("Cauly 获取url失败 appId:{}", adApp.getId());
                throw new RuntimeException();
            }
            logger.info("Cauly推广url:{}", url);
            resp.sendRedirect(url);
        } catch (IOException e)
        {
            logger.error("Cauly 推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
        } catch (Exception e)
        {
            logger.error("Cauly 推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;

        }


    }

    public String getUrl(AdApp adApp, AdClick adClick)
    {
        // 获得host 参数
        String host = UrlUtils.UrlPage(adApp.getLeagueUrl());
        Map<String, String> params = UrlUtils.URLRequest(adApp.getLeagueUrl());

        if(Constant.PHONE_TYPE_ANDROID == adClick.getPhoneType())
        {
            logger.info("Cauly 推广 安卓 clickId:{} 未开发", adClick.getId());
            return null;

        }
        else
        {
            logger.info("Cauly 推广 ios clickId:{}", adClick.getId());
            params.put("scode", adClick.getIdfa());
        }

        params.put("subpartner_id", "00"); // 固定参数
        params.put("redirect_type", "ht"); // 固定参数
        params.put("partner_data", String.valueOf(adClick.getId()));


        // 拼接URL
        return CommonUtils.getFullUrl(host, params);

    }
}
