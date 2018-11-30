package com.xqh.ad.utils;

import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.entity.other.IdfaReportMqDTO;
import com.xqh.ad.service.league.CustomAdService;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.tkmapper.mapper.AdOdsIdfaReportMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by hssh on 2017/8/25.
 */
@Component
public class AsyncUtils
{
    public static Logger logger = LoggerFactory.getLogger(AsyncUtils.class);

    @Resource
    private CustomAdService customAdService;
    @Resource
    private AdClickMapper adClickMapper;
    @Resource
    private AdOdsIdfaReportMapper adOdsIdfaReportMapper;

    /**
     * 回调
     */
    @Async
    public void callbackUser(String callback)
    {
        logger.info("异步回调开始 callback:{}", callback);
        String url = null;
        try
        {
            url = URLDecoder.decode(callback, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("异步回到异常 callback:{}", callback);
            return ;
        }

        logger.info("异步回调url:{}", url);

        HttpResult httpResult = HttpUtils.get(url);

        logger.info("异步回调结果 httpResult:{}", httpResult);

    }

    @Async
    public void report(String reportUrl) {
        HttpResult httpResult = HttpUtils.get(reportUrl);
        logger.info("蹭量上报地址：{} 返回值状态码:{}", reportUrl, httpResult.getStatus());
    }

    @Async
    public void idfaReportAndSaveAdClick(IdfaReportMqDTO idfaReportMqDTO, AdApp adApp, AdLeague adLeague, AdClick adClick)
    {
        // 保存点击记录
        adClickMapper.insertSelective(adClick);

        // 获取上报地址 并上报
        String reportUrl;
        try
        {
            reportUrl = customAdService.getUrl(adApp, adClick, adLeague);
        } catch (UnsupportedEncodingException e)
        {
            logger.error("idfa上报消费者异步处理 获取上报地址异常 idfaReportMqDTO:{}", JSONObject.toJSON(idfaReportMqDTO));
            return ;
        }
        HttpResult httpResult = HttpUtils.get(reportUrl);
        logger.info("idfa上报消费者异步处理 上报地址:{} 返回值状态码:{}", reportUrl, httpResult.getStatus());


        // 删除上报记录
        if (StringUtils.isBlank(idfaReportMqDTO.getTableIndex())) {
            adOdsIdfaReportMapper.deleteByPrimaryKey(idfaReportMqDTO.getId());
        } else {
            adOdsIdfaReportMapper.deleteById(idfaReportMqDTO.getTableIndex(), idfaReportMqDTO.getId());
        }

    }

}
