package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.controller.api.ITencentAdController;
import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdDownload;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.tkmapper.mapper.AdDownloadMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.common.DozerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

/**
 * Created by hssh on 2017/8/20.
 */
@RestController
public class TencentAdController implements ITencentAdController
{
    public static Logger logger = LoggerFactory.getLogger(TencentAdController.class);

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
        logger.info("腾讯 callback params:{}", JSONObject.toJSON(params));

        String schId = req.getParameter("sch_id");

        if(!StringUtils.isNumeric(schId))
        {
            logger.error("腾讯回调异常 schId{}不合法 ", schId);
            return ;
        }

        AdClick adClick = adClickMapper.selectByPrimaryKey(Integer.valueOf(schId));

        if(null == adClick)
        {
            logger.error("腾讯回调异常 schId{}不合法 ", schId);
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

        logger.info("腾讯回调开始 schId:{}", schId);

        xqhAdService.callbackUser(adClick.getCallbackUrl());
    }
}
