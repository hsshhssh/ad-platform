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
public class VirtualAdService extends LeagueAbstractService
{
    private static Logger logger = LoggerFactory.getLogger(VirtualAdService.class);

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick)
    {

        logger.info("虚拟联盟url appid:{} url:{}", adApp.getId(), adApp.getLeagueUrl());

        try
        {
            String url = getUrl(adApp, adClick);
            if(StringUtils.isBlank(url))
            {
                throw new RuntimeException("获取url失败");
            }
            logger.info("调用虚拟联盟推广url:{}", url);
            resp.sendRedirect(url);
        }
        catch (IOException e)
        {
            logger.error("虚拟联盟推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;
        }
        catch (Exception e)
        {
            logger.error("虚拟联盟推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
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
            logger.info("虚拟联盟 andrond 未开发 clickId:{}", adClick.getId());
            return null;
        }
        else
        {
            logger.info("虚拟联盟 ios clickId:{}", adClick.getId());
            params.put("idfa", adClick.getIdfa());
        }

        params.put("appid", String.valueOf(adApp.getId()));
        params.put("clickid", String.valueOf(adClick.getId()));

        return CommonUtils.getFullUrl(host, params);

    }
}
