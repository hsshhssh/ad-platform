package com.xqh.ad.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.xqh.ad.tkmapper.entity.AdDaySettlement;
import com.xqh.ad.tkmapper.entity.AdMonthSettlement;
import com.xqh.ad.tkmapper.mapper.AdDaySettlementMapper;
import com.xqh.ad.tkmapper.mapper.AdMonthSettlementMapper;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.constant.MonthSettlementSourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by hssh on 2018/3/31.
 */
@Component
@Slf4j
public class MonthSettlementJobs
{

    @Resource
    private AdDaySettlementMapper adDaySettlementMapper;
    @Resource
    private AdMonthSettlementMapper adMonthSettlementMapper;

    @Scheduled(cron = "0 40 0 1 * ?")
    public void settlement() {
        long nowTime = System.currentTimeMillis() / 1000;
        log.info("每月结算自动任务开始 当前时间={}", nowTime);

        List<Integer> yearAndMonth = CommonUtils.getLastMonth();
        List<Integer> startAndEndTime = CommonUtils.getMonthStartEndTime(yearAndMonth.get(0), yearAndMonth.get(1));
        List<AdMonthSettlement> monthSettlementList = getMonthSettlementByTime(startAndEndTime.get(0), startAndEndTime.get(1));
        log.info("上个月年份={} 上个月月份={} 上个月开始时间={} 上个月结束时间={}", yearAndMonth.get(0), yearAndMonth.get(1), startAndEndTime.get(0), startAndEndTime.get(1));

        for (AdMonthSettlement monthSettlement : monthSettlementList)
        {
            adMonthSettlementMapper.insertSelective(monthSettlement);
        }

        log.info("每月结算自动任务结算 当前时间={}", nowTime);
    }


    public List<AdMonthSettlement> getMonthSettlementByTime(int startTime, int endTime) {
        // 获取日结算记录
        Search search = new Search();
        search.put("createTime_gte", startTime);
        search.put("createTime_lt", endTime);
        Example example = new ExampleBuilder(AdDaySettlement.class).search(search).sort(Collections.singletonList("id_desc")).build();
        List<AdDaySettlement> daySettlementList = adDaySettlementMapper.selectByExample(example);

        // appMediaId => 日结算记录
        Multimap<Integer, AdDaySettlement> daySettlementMultimap = ArrayListMultimap.create();
        for (AdDaySettlement daySettlement : daySettlementList)
        {
            daySettlementMultimap.put(daySettlement.getAppMediaId(), daySettlement);
        }

        // 计算adMonthSettlement集合
        List<AdMonthSettlement> monthSettlementList = Lists.newArrayList();
        for (Integer appMediaId : daySettlementMultimap.keySet())
        {
            AdMonthSettlement adMonthSettlement = buildAdMonthSettlement(startTime, daySettlementMultimap.get(appMediaId));
            monthSettlementList.add(adMonthSettlement);
        }

        return monthSettlementList;
    }

    /**
     * 构造月结算记录
     */
    public AdMonthSettlement buildAdMonthSettlement(int startTime, Collection<AdDaySettlement> daySettlementList) {
        int totalDownload = 0;
        int skipDownload = 0;
        int callbackDownload = 0;
        int mediaId = 0, appId = 0, leagueId = 0;

        for (AdDaySettlement daySettlement : daySettlementList)
        {
            totalDownload += daySettlement.getDownloadCount();
            callbackDownload += daySettlement.getCallbackDownloadCount();
            skipDownload += daySettlement.getDownloadCount() - daySettlement.getCallbackDownloadCount();
            mediaId = daySettlement.getMediaId();
            appId = daySettlement.getAppId();
            leagueId = daySettlement.getLeagueId();
        }

        AdMonthSettlement monthSettlement = new AdMonthSettlement();
        monthSettlement.setSource(MonthSettlementSourceEnum.SYSTEM.getCode());
        monthSettlement.setPutTime(startTime);
        monthSettlement.setMediaId(mediaId);
        monthSettlement.setLeagueId(leagueId);
        monthSettlement.setAppId(appId);
        monthSettlement.setTotalDownload(totalDownload);
        monthSettlement.setSkipDownload(skipDownload);
        monthSettlement.setCallbackDownload(callbackDownload);
        int nowTime = (int) (System.currentTimeMillis()/1000);
        monthSettlement.setCreateTime(nowTime);
        monthSettlement.setUpdateTime(nowTime);

        return monthSettlement;
    }

}
