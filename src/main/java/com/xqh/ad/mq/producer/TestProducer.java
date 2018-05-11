package com.xqh.ad.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by hssh on 2018/4/15.
 */
@Component
@Slf4j
public class TestProducer
{
    private final String QUEUE_NAME = "test";

    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(String msg)
    {
        log.info("发送消息: {}", msg);
        amqpTemplate.convertAndSend(QUEUE_NAME, msg);
    }
}
