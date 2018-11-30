package com.xqh.ad.jobs;

import com.xqh.ad.service.IdfaJobsService;
import com.xqh.ad.utils.condition.IdfaReportJobsCondition2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by hssh on 2018/6/4.
 */
@Slf4j
@Component
@Conditional(IdfaReportJobsCondition2.class)
public class IdfaJobs2
{
    @Autowired
    private IdfaJobsService idfaJobsService;

    @Scheduled(cron = "2/5 * * * * ? ")
    public void getAndSendIdfa() {
        idfaJobsService.getAndSendIdfa("2");
    }

}
