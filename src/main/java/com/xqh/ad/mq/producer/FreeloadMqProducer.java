package com.xqh.ad.mq.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.xqh.ad.entity.other.FreeloadMqDTO;
import com.xqh.ad.mq.RabbitConfig;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.condition.ConditionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
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
        if(isAllowSend()) {
            log.info("蹭量消息队列 发送消息:{}", JSON.toJSON(freeloadMqDTO));
            try
            {
                amqpTemplate.convertAndSend(RabbitConfig.FREELOAD_MQ, JSONObject.toJSONString(freeloadMqDTO));
            } catch (AmqpException e)
            {
                log.info("蹭量消息发送异常 消息：{}, {}", JSON.toJSON(freeloadMqDTO), Throwables.getStackTraceAsString(e));
            }
        } else {
            log.info("消息开关未打开 放弃发送:{}", JSON.toJSON(freeloadMqDTO));
        }

    }

    public boolean isAllowSend() {
        return Constant.CONDITION_PASS_FLAG.equals(System.getProperty(ConditionConstants.MQPRODUCER_CONDITION));
    }

}
