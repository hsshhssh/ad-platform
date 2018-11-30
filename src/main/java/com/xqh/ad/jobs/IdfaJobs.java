package com.xqh.ad.jobs;

import com.google.common.base.Splitter;
import com.xqh.ad.entity.other.IdfaReportMqDTO;
import com.xqh.ad.mq.producer.IdfaReportMqProducer;
import com.xqh.ad.tkmapper.entity.AdOdsIdfaReport;
import com.xqh.ad.tkmapper.mapper.AdOdsIdfaReportMapper;
import com.xqh.ad.utils.config.AdIdfaReportConfigUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.condition.IdfaReportJobsCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hssh on 2018/6/4.
 */
@Slf4j
@Component
@Conditional(IdfaReportJobsCondition.class)
public class IdfaJobs
{

    @Resource
    private AdOdsIdfaReportMapper adOdsIdfaReportMapper;
    @Resource
    private IdfaReportMqProducer idfaReportMqProducer;
    @Resource
    private AdIdfaReportConfigUtils adIdfaReportConfigUtils;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void getAndSendIdfa() {
        long starTime = System.currentTimeMillis();
        log.info("蹭量自动任务开始 startTime:{}", starTime);

        // 根据当前时间获取捞取的条数
        Integer count = getCountByCurrentTime();
        log.info("蹭量自动任务 获取条数：{}", count);

        // 获取上报数据
        List<AdOdsIdfaReport> idfaList = getIdfaList(count);
        if(CollectionUtils.isEmpty(idfaList)) {
            log.info("idfa上报自动任务 没有需要上报的idfa");
            return ;
        }

        // 发送消息
        sendMqMsg(idfaList);

        // 修改为已发送状态
        updateSentState(idfaList);

        long endTime = System.currentTimeMillis();
        log.info("蹭量自动任务结束 endTime:{} 耗时:{}ms", endTime, endTime - starTime);
    }

    /**
     * 根据当前时间获取捞取的条数
     */
    private Integer getCountByCurrentTime()
    {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return adIdfaReportConfigUtils.getMinValue(hour, minute);
    }

    private void sendMqMsg(List<AdOdsIdfaReport> idfaList) {
        for (AdOdsIdfaReport idfaReport : idfaList)
        {
            List<String> appMedisIdList = Splitter.on(",").omitEmptyStrings().splitToList(idfaReport.getAppMediaId());
            for (String appMediaId : appMedisIdList) {
                try {
                    IdfaReportMqDTO idfaReportMqDTO = new IdfaReportMqDTO();
                    idfaReportMqDTO.setAppMediaId(Integer.valueOf(appMediaId));
                    idfaReportMqDTO.setIp(idfaReport.getIp());
                    idfaReportMqDTO.setIdfa(idfaReport.getIdfa());
                    idfaReportMqDTO.setId(idfaReport.getId());
                    idfaReportMqProducer.send(idfaReportMqDTO);
                } catch (NumberFormatException e) {
                    log.error("配置异常 idfaReportId:{} appMediaId:{}", idfaReport.getId(), idfaReport.getAppMediaId(), e);
                    // 已发送到消息队列
                    AdOdsIdfaReport record = AdOdsIdfaReport.builder().id(idfaReport.getId()).state(1).build();
                    adOdsIdfaReportMapper.updateByPrimaryKeySelective(record);
                }
            }
        }
    }

    private void updateSentState(List<AdOdsIdfaReport> idfaList)
    {
        List<Integer> ids = new ArrayList<>(idfaList.size());
        for (AdOdsIdfaReport idfaReport : idfaList)
        {
            ids.add(idfaReport.getId());
        }
        Search search = new Search();
        search.put("id_in", ids);
        Example example = new ExampleBuilder(AdOdsIdfaReport.class).search(search).build();

        // 已发送到消息队列
        AdOdsIdfaReport record = new AdOdsIdfaReport();
        record.setState(1);

        adOdsIdfaReportMapper.updateByExampleSelective(record,example);
    }

    private List<AdOdsIdfaReport> getIdfaList(int count) {
        AdOdsIdfaReport adOdsIdfaReport = new AdOdsIdfaReport();
        adOdsIdfaReport.setState(0);
        return adOdsIdfaReportMapper.selectByRecordAndLimitRaw(adOdsIdfaReport, count);
    }
}
