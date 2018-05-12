package com.xqh.ad.mq.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.entity.other.FreeloadMqDTO;
import com.xqh.ad.mq.RabbitConfig;
import com.xqh.ad.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by hssh on 2018/5/11.
 */
@Component
@Slf4j
public class FreeloadMqProducer
{
    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(FreeloadMqDTO freeloadMqDTO) {
        log.info("蹭量消息队列 发送消息:{}", JSON.toJSON(freeloadMqDTO));
        amqpTemplate.convertAndSend(RabbitConfig.FREELOAD_MQ, JSONObject.toJSONString(freeloadMqDTO));
    }

    public boolean isAllowSend() {
        return Constant.CONDITION_PASS_FLAG.equals(System.getProperty("ad.condition.mqproducer"));
    }

}
