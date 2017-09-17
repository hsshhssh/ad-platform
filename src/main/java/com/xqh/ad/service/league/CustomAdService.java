package com.xqh.ad.service.league;

import com.google.common.base.Throwables;
import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.tkmapper.entity.*;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.tkmapper.mapper.AdLeagueReportConfigMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.HttpsUtils;
import com.xqh.ad.utils.UrlUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.constant.ReportTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/9/17.
 */
@Service
public class CustomAdService extends LeagueAbstractService
{
    private static Logger logger = LoggerFactory.getLogger(CustomAdService.class);

    @Autowired
    private AdLeagueMapper adLeagueMapper;

    @Autowired
    private AdLeagueReportConfigMapper adLeagueReportConfigMapper;

    public boolean isCustomLeague(int leagueId)
    {
        AdLeague adLeague = adLeagueMapper.selectByPrimaryKey(leagueId);

        if(null == adLeague)
        {
            logger.info("leagueId:{} 无配置联盟信息");
            return false;
        }

        logger.info("leagueId:{} 有配置联盟信息 联盟通道:{}", adLeague);
        return true;
    }

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp,AdApp adApp, AdAppMedia adAppMedia, AdClick adClick )
    {

        try
        {
            String reportUrl = getUrl(adApp, adAppMedia, adClick); // 上报地址
            String redirectUrl = null; // 下载跳转地址
            if(StringUtils.isBlank(reportUrl))
            {
                throw new RuntimeException("配置联盟 上报地址为空");
            }
            if(ReportTypeEnum._S2S.getValue() == adApp.getReportType())
            {
                logger.info("配置联盟 上报方式:s2s appId:{} reportUrl:{}", adApp.getId(), reportUrl);
                // 上报
                HttpResult httpResult = HttpsUtils.get(reportUrl, "UTF-8");
                logger.info("配置联盟 上报返回值 httpResult:{}", httpResult);
                redirectUrl = adApp.getRedirectUrl();
            }
            else
            {
                logger.info("配置联盟 上报方式:302 appId:{}", adApp.getId());
                // 跳转地址就是上报地址
                redirectUrl = reportUrl;
            }
            logger.info("调用配置联盟推广url:{}", redirectUrl);
            resp.sendRedirect(redirectUrl);
        }
        catch (IOException e)
        {
            logger.info("配置通道 异常 e:{}", Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
        }
        catch (Exception e)
        {

            logger.info("配置通道 异常 e:{}", Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
        }

    }


    public String getUrl(AdApp adApp, AdAppMedia adAppMedia, AdClick adClick)
    {
        // 获取key值对应列表
        Search searchConfig = new Search();
        searchConfig.put("leagueId", adApp.getLeagueId());
        List<AdLeagueReportConfig> configList = adLeagueReportConfigMapper.selectByExample(new ExampleBuilder(AdLeagueReportConfig.class).search(searchConfig).build());

        // 获取上报地址 原始参数
        String baseHost = UrlUtils.UrlPage(adApp.getLeagueUrl());
        Map<String, String> params = UrlUtils.URLRequest(adApp.getLeagueUrl());

        // 获取配置参数
        for (AdLeagueReportConfig reportConfig : configList)
        {
            if(Constant.CALLBACK_CLICK_ID.equals(reportConfig.getXqhKey()))
            {
                continue;
            }

            String xqhKeyValue = null;
            try
            {
                xqhKeyValue = String.valueOf(AdClick.class.getField(reportConfig.getXqhKey()));
            } catch (NoSuchFieldException e)
            {
                logger.info("配置信息有误 reportConfig:{} e:{}", reportConfig, Throwables.getStackTraceAsString(e));

                throw new RuntimeException("配置信息有误");
            }

            params.put(reportConfig.getLeagueKey(), xqhKeyValue);
        }

        return CommonUtils.getFullUrl(baseHost, params);
    }

}
