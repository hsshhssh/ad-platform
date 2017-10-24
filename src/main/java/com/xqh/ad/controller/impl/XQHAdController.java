package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.xqh.ad.controller.api.IXQHAdController;
import com.xqh.ad.entity.other.S2SReportResult;
import com.xqh.ad.exception.NoLeagueChannelException;
import com.xqh.ad.exception.RequestParamException;
import com.xqh.ad.service.AdAppMediaService;
import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.service.league.LeagueAbstractService;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.ConfigUtils;
import com.xqh.ad.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hssh on 2017/8/12.
 */
@RestController
public class XQHAdController implements IXQHAdController
{
    private static Logger logger = LoggerFactory.getLogger(XQHAdController.class);

    @Autowired
    private AdAppMediaService adAppMediaService;

    @Autowired
    private AdAppMapper adAppMapper;

    @Autowired
    private XQHAdService xqhAdService;

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private AdClickMapper adClickMapper;

    @Autowired
    private AdAppMediaMapper adAppMediaMapper;

    @Override
    @Transactional
    public void url(HttpServletRequest req, HttpServletResponse resp, String urlCode)
    {
        // 判断是否在黑名单内
        if(configUtils.getUrlCodeBlackList().contains(urlCode))
        {
            logger.error("urlCode:{} 黑名单", urlCode);
            CommonUtils.writeResponse(resp, Constant.BLACK_LIST);
            return ;
        }

        AdAppMedia adAppMedia = adAppMediaService.getByUrlCode(urlCode);

        // 判断url
        if(null == adAppMedia)
        {
            logger.error("urlCode:{} 无效urlCode", urlCode);
            CommonUtils.writeResponse(resp, Constant.ERROR_CODE_URL);
            return ;
        }


        // 判断key
        String key = req.getParameter("key");
        if(!adAppMedia.getAppKey().equals(key))
        {
            logger.error("urlCode:{} key:{} 无效key", urlCode, key);
            CommonUtils.writeResponse(resp, Constant.ERROR_CODE_KEY);
            return ;
        }

        // 判断联盟通道
        AdApp adApp = adAppMapper.selectByPrimaryKey(adAppMedia.getAppId());
        if(null == adApp)
        {
            logger.error("urlCode:{} key:{} adAppMedia:{} 无效通道", urlCode, key, adAppMedia);
            CommonUtils.writeResponse(resp, Constant.ERROR_CHANNEL);
            return;
        }


        // 选择联盟通道
        LeagueAbstractService leagueAbstractService = null;
        try
        {
            leagueAbstractService = xqhAdService.dispatchLeague(adApp.getLeagueCode(), adApp.getLeagueId());
        }
        catch (NoLeagueChannelException e)
        {
            logger.error("无联盟通道 e:{}", Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_CHANNEL);
            return;
        }


        // 保存点击记录
        AdClick adClick = null;
        try
        {
            adClick = leagueAbstractService.insertClickRecord(req, adAppMedia, adApp);
        }
        catch (RequestParamException e)
        {
            logger.error("请求参数异常 e:{}", Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_PARAM);
            return ;
        }

        String reportTypeStr = (req.getParameter("report_type"));
        if(null != reportTypeStr && 2 == Integer.valueOf(reportTypeStr) && 1 == adApp.getReportType())
        {
            logger.info("下游s2s方式 上游也是302方式 reportType:{} ", reportTypeStr);
            S2SReportResult reportResult = new S2SReportResult("success", String.valueOf(adClick.getId()));
            CommonUtils.writeResponse(resp, JSONObject.toJSONString(reportResult));
        }
        else
        {
            logger.info("下游方式：{} 上游方式：{}", reportTypeStr, adApp.getReportType());
            leagueAbstractService.redirectUrl(req, resp, adApp, adAppMedia, adClick, reportTypeStr);
        }

        return ;

    }

    @Override
    public void redirect(HttpServletRequest req,
                         HttpServletResponse resp,
                         @PathVariable("token") String token)
    {
        logger.info("s2s方式 跳转地址 token:{}", token);

        AdClick adClick = adClickMapper.selectByPrimaryKey(Integer.valueOf(token));
        if(null == adClick)
        {
            logger.error("跳转方法 token：{} 无效 ", token);
            CommonUtils.writeResponse(resp, Constant.ERROR_TOKEN);
            return ;
        }

        AdApp adApp = adAppMapper.selectByPrimaryKey(adClick.getAppId());
        AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(adClick.getAppMediaId());


        if(adApp.getReportType() == 2)
        {
            logger.info("跳转地址 上游s2s方式 直接跳转");
            try
            {
                resp.sendRedirect(adApp.getRedirectUrl());
            } catch (IOException e)
            {
                logger.info("跳转异常 e:{}", Throwables.getStackTraceAsString(e));
                return;
            }
        }
        else
        {
            logger.info("跳转地址 上游302方式 上报并跳转");

            // 选择联盟通道
            LeagueAbstractService leagueAbstractService = null;
            try
            {
                leagueAbstractService = xqhAdService.dispatchLeague(adApp.getLeagueCode(), adApp.getLeagueId());
            }
            catch (NoLeagueChannelException e)
            {
                logger.error("无联盟通道 e:{}", Throwables.getStackTraceAsString(e));
                CommonUtils.writeResponse(resp, Constant.ERROR_CHANNEL);
                return;
            }


            leagueAbstractService.redirectUrl(req, resp, adApp, adAppMedia, adClick, null);

            return;
        }

    }
}
