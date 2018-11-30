package com.xqh.ad.service.league;

import com.google.common.base.Throwables;
import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.HttpsUtils;
import com.xqh.ad.utils.UrlUtils;
import com.xqh.ad.utils.enums.ReportTypeEnum;
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
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick, String reportTypeParam)
    {

        logger.info("虚拟联盟url appid:{} url:{}", adApp.getId(), adApp.getLeagueUrl());

        try
        {
            String reportUrl = getUrl(adApp.getLeagueUrl(), adClick, adApp.getId()); // 上报地址
            String redirectUrl = null; // 下载跳转地址
            if(StringUtils.isBlank(reportUrl))
            {
                logger.error("获取上报地址失败 appId:{} adClick:{}", adApp.getId(), adClick);
                throw new RuntimeException("获取url失败");
            }

            if(ReportTypeEnum._S2S.getValue() == adApp.getReportType())
            {
                logger.info("上报方式:s2s appId:{} reportUrl:{}", adApp.getId(), reportUrl);
                // 上报
                HttpResult httpResult = HttpsUtils.get(reportUrl, "UTF-8");
                logger.info("上报返回值 httpResult:{}", httpResult);
                redirectUrl = adApp.getRedirectUrl();
            }
            else
            {
                logger.info("上报方式:302 appId:{}", adApp.getId());
                // 跳转地址就是上报地址
                redirectUrl = reportUrl;
            }
            logger.info("调用虚拟联盟推广url:{}", redirectUrl);
            resp.sendRedirect(redirectUrl);
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

    public String getUrl(String reportUrl, AdClick adClick, int appId)
    {
        // 获得域名
        String host = UrlUtils.UrlPage(reportUrl);
        Map<String, String> params = UrlUtils.URLRequest(reportUrl);

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

        params.put("appid", String.valueOf(appId));
        params.put("clickid", String.valueOf(adClick.getId()));

        return CommonUtils.getFullUrl(host, params);

    }
}
