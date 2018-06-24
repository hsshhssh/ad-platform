package com.xqh.ad.jobs;

import com.xqh.ad.entity.other.IdfaReportMqDTO;
import com.xqh.ad.mq.producer.IdfaReportMqProducer;
import com.xqh.ad.tkmapper.entity.AdOdsIdfaReport;
import com.xqh.ad.tkmapper.mapper.AdOdsIdfaReportMapper;
import com.xqh.ad.utils.ConfigUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.condition.IdfaReportJobsCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private ConfigUtils configUtils;
    @Resource
    private IdfaReportMqProducer idfaReportMqProducer;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void getAndSendIdfa() {
        // 获取上报数据
        List<AdOdsIdfaReport> idfaList = getIdfaList();
        if(CollectionUtils.isEmpty(idfaList)) {
            log.info("idfa上报自动任务 没有需要上报的idfa");
            return ;
        }

        // 发送消息
        sendMqMsg(idfaList);

        // 修改为已发送状态
        updateSentState(idfaList);

    }

    private void sendMqMsg(List<AdOdsIdfaReport> idfaList) {
        for (AdOdsIdfaReport idfaReport : idfaList)
        {
            IdfaReportMqDTO idfaReportMqDTO = new IdfaReportMqDTO();
            idfaReportMqDTO.setAppMediaId(idfaReport.getAppMediaId());
            idfaReportMqDTO.setIp(idfaReport.getIp());
            idfaReportMqDTO.setIdfa(idfaReport.getIdfa());
            idfaReportMqDTO.setId(idfaReport.getId());
            idfaReportMqProducer.send(idfaReportMqDTO);
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

    private List<AdOdsIdfaReport> getIdfaList() {
        Search search = new Search();
        search.put("state_eq", 0);
        Example example = new ExampleBuilder(AdOdsIdfaReport.class).search(search).build();
        return adOdsIdfaReportMapper.selectByExampleAndRowBounds(example, new RowBounds(0, Integer.valueOf(configUtils.getGetReportIdfaMaxSize())));
    }
}
