package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.controller.api.IReYunAdController;
import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.service.league.LeagueAbstractService;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdDownload;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.tkmapper.mapper.AdDownloadMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.HttpUtils;
import com.xqh.ad.utils.common.DozerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.TreeMap;

/**
 * Created by hssh on 2017/8/13.
 */
@RestController
public class ReYunAdController implements IReYunAdController {

    private static Logger logger = LoggerFactory.getLogger(ReYunAdController.class);

    @Autowired
    private AdClickMapper adClickMapper;

    @Autowired
    private AdDownloadMapper adDownloadMapper;

    @Autowired
    private XQHAdService xqhAdService;

    @Override
    @Transactional
    public void callback(HttpServletRequest req, HttpServletResponse resp)
    {
        TreeMap<String, String> params = CommonUtils.getParams(req);
        logger.info("热云 callback params:{}", JSONObject.toJSON(params));

        String clickIdStr = req.getParameter("clickId");

        if(!StringUtils.isNumeric(clickIdStr))
        {
            logger.error("热云回调异常 clickId{}不合法 ", clickIdStr);
            return ;
        }

        AdClick adClick = adClickMapper.selectByPrimaryKey(Integer.valueOf(clickIdStr));

        if(null == adClick)
        {
            logger.error("热云回调异常 clickId{}不合法 ", clickIdStr);
            return ;
        }

        // 插入下载记录
        int nowTime = (int) (System.currentTimeMillis()/1000);
        AdDownload adDownload = DozerUtils.map(adClick, AdDownload.class);
        adDownload.setId(null);
        adDownload.setClickId(adClick.getId());
        adDownload.setCreateTime(nowTime);
        adDownload.setUpdateTime(nowTime);

        adDownloadMapper.insertSelective(adDownload);

        logger.info("异步回调开始 clickId:{}", clickIdStr);

        xqhAdService.callbackUser(adClick.getCallbackUrl());

    }

}
