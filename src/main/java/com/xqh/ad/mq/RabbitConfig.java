package com.xqh.ad.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hssh on 2018/4/15.
 */
@Configuration
public class RabbitConfig
{

    /**
     * 蹭量消息队列
     */
    public static final String FREELOAD_MQ = "freeload_mq";

    @Bean
    public Queue testQueue() {
        return new Queue("test");
    }

    @Bean
    public Queue freeloadQueue() {
        return new Queue(FREELOAD_MQ);
    }

}
