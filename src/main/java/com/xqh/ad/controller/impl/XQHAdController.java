package com.xqh.ad.controller.impl;

import com.google.common.base.Throwables;
import com.xqh.ad.controller.api.IXQHAdController;
import com.xqh.ad.exception.NoLeagueChannelException;
import com.xqh.ad.exception.RequestParamException;
import com.xqh.ad.service.AdAppMediaService;
import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.service.league.LeagueAbstractService;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Override
    @Transactional
    public void url(HttpServletRequest req, HttpServletResponse resp, String urlCode)
    {

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
            leagueAbstractService = xqhAdService.dispatchLeague(adApp.getLeagueCode());
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

        leagueAbstractService.redirectUrl(req, resp, adApp, adAppMedia, adClick);


        return ;

    }
}
