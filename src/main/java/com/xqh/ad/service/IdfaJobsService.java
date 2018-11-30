package com.xqh.ad.service;

import com.google.common.base.Splitter;
import com.xqh.ad.entity.other.IdfaReportMqDTO;
import com.xqh.ad.mq.producer.IdfaReportMqProducer;
import com.xqh.ad.tkmapper.entity.AdOdsIdfaReport;
import com.xqh.ad.tkmapper.mapper.AdOdsIdfaReportMapper;
import com.xqh.ad.utils.config.AdIdfaReportConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class IdfaJobsService {
    @Resource
    private AdOdsIdfaReportMapper adOdsIdfaReportMapper;
    @Resource
    private IdfaReportMqProducer idfaReportMqProducer;
    @Resource
    private AdIdfaReportConfigUtils adIdfaReportConfigUtils;

    public void getAndSendIdfa(String tableIndex) {
        long starTime = System.currentTimeMillis();
        log.info("蹭量自动任务开始 tableIndex:{} startTime:{}", tableIndex, starTime);

        // 根据当前时间获取捞取的条数
        Integer count = getCountByCurrentTime(tableIndex);
        log.info("蹭量自动任务 tableIndex:{} 获取条数：{}", tableIndex, count);

        // 获取上报数据
        List<AdOdsIdfaReport> idfaList = getIdfaList(tableIndex, count);
        if(CollectionUtils.isEmpty(idfaList)) {
            log.info("idfa上报自动任务 没有需要上报的idfa tableIndex:{}", tableIndex);
            return ;
        }

        // 发送消息
        sendMqMsg(tableIndex, idfaList);

        // 修改为已发送状态
        updateSentState(tableIndex, idfaList);

        long endTime = System.currentTimeMillis();
        log.info("蹭量自动任务结束 tableIndex:{} endTime:{} 耗时:{}ms", tableIndex, endTime, endTime - starTime);
    }


    /**
     * 根据当前时间获取捞取的条数
     */
    private Integer getCountByCurrentTime(String tableIndex)
    {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return adIdfaReportConfigUtils.getMinValue(tableIndex, hour, minute);
    }

    private void sendMqMsg(String tableIndex, List<AdOdsIdfaReport> idfaList) {
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
                    idfaReportMqDTO.setTableIndex(tableIndex);
                    idfaReportMqProducer.send(idfaReportMqDTO);
                } catch (NumberFormatException e) {
                    log.error("配置异常 idfaReportId:{} appMediaId:{}", idfaReport.getId(), idfaReport.getAppMediaId(), e);
                    // 已发送到消息队列
                    AdOdsIdfaReport record = AdOdsIdfaReport.builder().state(1).build();
                    adOdsIdfaReportMapper.updateByIds(tableIndex, record, Collections.singletonList(idfaReport.getId()));
                }
            }
        }
    }

    private void updateSentState(String tableName, List<AdOdsIdfaReport> idfaList)
    {
        List<Integer> ids = new ArrayList<>(idfaList.size());
        for (AdOdsIdfaReport idfaReport : idfaList)
        {
            ids.add(idfaReport.getId());
        }

        // 已发送到消息队列
        AdOdsIdfaReport record = new AdOdsIdfaReport();
        record.setState(1);

        adOdsIdfaReportMapper.updateByIds(tableName, record, ids);
    }

    private List<AdOdsIdfaReport> getIdfaList(String tableIndex, int count) {
        AdOdsIdfaReport adOdsIdfaReport = new AdOdsIdfaReport();
        adOdsIdfaReport.setState(0);
        return adOdsIdfaReportMapper.selectByRecordAndLimit(tableIndex, adOdsIdfaReport, count);
    }
}
