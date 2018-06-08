package com.xqh.ad.mq.producer;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.xqh.ad.entity.other.IdfaReportMqDTO;
import com.xqh.ad.mq.RabbitConfig;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.condition.ConditionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by hssh on 2018/6/4.
 */
@Slf4j
@Component
public class IdfaReportMqProducer
{
    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(IdfaReportMqDTO idfaReportMqDTO) {
        if(isAllowSend()) {
            log.info("idfa库上报生产者 消息队列发送消息：{}", JSON.toJSONString(idfaReportMqDTO));
            try
            {
                amqpTemplate.convertAndSend(RabbitConfig.IDFA_REPORT_MQ, JSON.toJSONString(idfaReportMqDTO));
            } catch (AmqpException e)
            {
                log.error("idfa库上报生产者 消息发送异常 msg:{} e:{}", JSON.toJSONString(idfaReportMqDTO), Throwables.getStackTraceAsString(e));
            }
        } else {
            log.info("idfa库上报生产者关闭 不发送消息 msg:{}", JSON.toJSONString(idfaReportMqDTO));
        }

    }

    public boolean isAllowSend() {
        return Constant.CONDITION_PASS_FLAG.equals(System.getProperty(ConditionConstants.IDFA_REPORT_MQPRODUCER_CONDITION));
    }


}
