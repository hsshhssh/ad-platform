package com.xqh.ad.utils.condition;

import com.xqh.ad.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by hssh on 2018/5/12.
 */
@Slf4j
public class JobsCondition implements Condition
{
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata)
    {
        String condition = System.getProperty(ConditionConstants.JOBS_CONDITION);
        if(Constant.CONDITION_PASS_FLAG.equals(condition)) {
            log.info("@@@@@@@@@@@@@---------------@@@@@@@@@");
            log.info("@@@@@@@@@@@@@定时任务开启@@@@@@@@@@@");
            log.info("@@@@@@@@@@@@@---------------@@@@@@@@@");
            return true;
        } else {
            log.info("@@@@@@@@@@@@@@定时任务关闭@@@@@@@@@@@@@@@");
            return false;
        }
    }
}
