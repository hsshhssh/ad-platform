package com.xqh.ad.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by hssh on 2018/4/15.
 */
@Component
@RabbitListener(queues = "test")
@Slf4j
public class TestConsumer
{
    @RabbitHandler
    public void process(Object o) {
        if (o instanceof String) {
            String hello = (String)o;
            log.info("消费者接受消息: {}", hello);
        } else {
            log.info("无效消息：{}", o);
        }
    }
}
