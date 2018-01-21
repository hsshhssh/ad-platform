package com.xqh.ad.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdDaySettlement;
import com.xqh.ad.tkmapper.entity.AdDownload;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.tkmapper.mapper.AdDaySettlementMapper;
import com.xqh.ad.tkmapper.mapper.AdDownloadMapper;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/5/13.
 */
@Component
public class Jobs
{
    private static Logger logger = LoggerFactory.getLogger(Jobs.class);

    @Autowired
    private AdClickMapper adClickMapper;

    @Autowired
    private AdDownloadMapper adDownloadMapper;

    @Autowired
    private AdAppMediaMapper adAppMediaMapper;

    @Autowired
    private AdDaySettlementMapper settlementMapper;

    @Scheduled(cron = "0 30 0 * * ? ")
    public void settlement()
    {
        logger.info("广告每日结算开始 nowTime:{}", System.currentTimeMillis());

        Map<Integer, Integer> clickMap = getClickMap(CommonUtils.getZeroHourTime(-1), CommonUtils.getZeroHourTime(0));

        Map<Integer, Integer> downloadMap = getDownloadMap(CommonUtils.getZeroHourTime(-1), CommonUtils.getZeroHourTime(0));

        Map<Integer, Integer> callbackDownloadMap = getCallbackDownloadMap(CommonUtils.getZeroHourTime(-1), CommonUtils.getZeroHourTime(0));

        int settlementTime = CommonUtils.getZeroHourTime(0);
        int nowTime = (int) (System.currentTimeMillis()/1000);


        for (Integer appMediaId : clickMap.keySet())
        {
            AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(appMediaId);
            if(adAppMedia == null) continue;

            AdDaySettlement settlement = new AdDaySettlement();
            settlement.setAppMediaId(appMediaId);
            settlement.setAppId(adAppMedia.getAppId());
            settlement.setMediaId(adAppMedia.getMediaId());
            settlement.setSettlementTime(settlementTime);
            settlement.setClickCount(clickMap.get(appMediaId) == null ? 0 : clickMap.get(appMediaId));
            settlement.setDownloadCount(downloadMap.get(appMediaId) == null ? 0 : downloadMap.get(appMediaId));
            settlement.setCallbackDownloadCount(callbackDownloadMap.get(appMediaId) == null ? 0 : callbackDownloadMap.get(appMediaId));
            settlement.setCreateTime(nowTime);
            settlement.setUpdateTime(nowTime);

            settlementMapper.insertSelective(settlement);
        }
    }


    public Map<Integer, Integer> getClickMap(int startTime, int endTime)
    {
        Search search = new Search();
        search.put("createTime_gte", startTime);
        search.put("createTime_lt", endTime);

        Example example = new ExampleBuilder(AdClick.class).search(search).fields(Arrays.asList("appMediaId","id")).build();

        List<AdClick> adClickList = adClickMapper.selectByExample(example);

        Multimap<Integer, AdClick> clickMultimap = ArrayListMultimap.create();

        for (AdClick adClick : adClickList)
        {
            clickMultimap.put(adClick.getAppMediaId(), adClick);
        }

        Map<Integer, Integer> clickMap = Maps.newHashMap();

        for (Integer appMediaId : clickMultimap.keySet())
        {
            clickMap.put(appMediaId, clickMultimap.get(appMediaId).size());
        }

        return clickMap;
    }


    public Map<Integer, Integer> getDownloadMap(int startTime, int endTime)
    {
        Search search = new Search();
        search.put("createTime_gte", startTime);
        search.put("createTime_lt", endTime);

        Example example = new ExampleBuilder(AdDownload.class).search(search).build();

        List<AdDownload> adDownloadList = adDownloadMapper.selectByExample(example);

        Multimap<Integer, AdDownload> downloadMultimap = ArrayListMultimap.create();

        for (AdDownload adDownload : adDownloadList)
        {
            downloadMultimap.put(adDownload.getAppMediaId(), adDownload);
        }

        Map<Integer, Integer> downLoadMap = Maps.newHashMap();

        for (Integer appMediaId : downloadMultimap.keySet())
        {
            downLoadMap.put(appMediaId, downloadMultimap.get(appMediaId).size());
        }

        return downLoadMap;

    }


    public Map<Integer, Integer> getCallbackDownloadMap(int startTime, int endTime)
    {
        Search search = new Search();
        search.put("createTime_gte", startTime);
        search.put("createTime_lt", endTime);
        search.put("isSkip_eq", 0);

        Example example = new ExampleBuilder(AdDownload.class).search(search).build();

        List<AdDownload> adDownloadList = adDownloadMapper.selectByExample(example);

        Multimap<Integer, AdDownload> downloadMultimap = ArrayListMultimap.create();

        for (AdDownload adDownload : adDownloadList)
        {
            downloadMultimap.put(adDownload.getAppMediaId(), adDownload);
        }

        Map<Integer, Integer> downLoadMap = Maps.newHashMap();

        for (Integer appMediaId : downloadMultimap.keySet())
        {
            downLoadMap.put(appMediaId, downloadMultimap.get(appMediaId).size());
        }

        return downLoadMap;

    }

}
