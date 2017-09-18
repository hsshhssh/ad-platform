package com.xqh.ad.utils.jobs;

import org.hssh.common.ZkdbcpConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by hssh on 2017/9/18.
 */
@Component
public class DataTransferJobs
{
    private static String bkDatabaseName;

    @Autowired
    private ZkdbcpConfigProperties properties;

    @Resource(name = "bkJdbcTemplate")
    private JdbcTemplate bkJdbcTemplate;

    @PostConstruct
    public void init()
    {
        bkDatabaseName = "bk" + properties.getBizName();
    }

    @Scheduled(cron = "0 30 2 * * ? ")
    public void dataTransfer()
    {
        // TODO 防重



    }


}
