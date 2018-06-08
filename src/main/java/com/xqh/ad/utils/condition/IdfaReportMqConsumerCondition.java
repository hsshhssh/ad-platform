package com.xqh.ad.utils.condition;

import com.xqh.ad.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by hssh on 2018/6/6.
 */
@Slf4j
public class IdfaReportMqConsumerCondition implements Condition
{
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata)
    {
        String condition = System.getProperty(ConditionConstants.IDFA_REPORT_MQCONSUMER_CONDITION);
        if(Constant.CONDITION_PASS_FLAG.equals(condition)) {
            log.info("@@@@@@@@@@@@@---------------@@@@@@@@@@@@@@@@");
            log.info("@@@@@@@@@@@@@idfa上报消息队列消费者开启@@@@@@@@");
            log.info("@@@@@@@@@@@@@---------------@@@@@@@@@@@@@@@@");
            return true;
        } else {
            log.info("@@@@@@@@@@@@@@idfa上报消息队列消费者关闭@@@@@@@@@@@@@@@");
            return false;
        }
    }
}
