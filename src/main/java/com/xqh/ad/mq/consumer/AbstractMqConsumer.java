package com.xqh.ad.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.lang.reflect.ParameterizedType;

/**
 * Created by hssh on 2018/6/6.
 */
@Slf4j
public abstract class AbstractMqConsumer<T>
{
    /**
     * 获取mq传输的对象
     */
    public T getMqDTO(Object o)
    {
        try
        {
            Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            T t = null;
            Message message = null;
            if(o instanceof String) {
                t = JSONObject.parseObject((String) o, tClass );
            } else if(o instanceof Message) {
                message = (Message) o;
                t = JSONObject.parseObject(new String(message.getBody()), tClass);
            } else {
                log.info("获取mq传输对象 消息无效 o:{}", JSONObject.toJSON(o));
                return null;
            }
            return t;
        } catch (Exception e)
        {
            log.info("获取mq传输对象 异常 o:{} e:{}", JSONObject.toJSON(o), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

}
