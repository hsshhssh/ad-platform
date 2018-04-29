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

    @Bean
    public Queue testQueue() {
        return new Queue("test");
    }

}
