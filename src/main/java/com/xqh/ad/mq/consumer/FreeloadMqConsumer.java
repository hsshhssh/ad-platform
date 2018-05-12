package com.xqh.ad.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.entity.other.FreeloadMqDTO;
import com.xqh.ad.entity.other.HttpResult;
import com.xqh.ad.mq.RabbitConfig;
import com.xqh.ad.service.league.CustomAdService;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.utils.HttpUtils;
import com.xqh.ad.utils.condition.MqConsumerCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * Created by hssh on 2018/5/11.
 */
@Component
@RabbitListener(queues = RabbitConfig.FREELOAD_MQ)
@Slf4j
@Conditional(MqConsumerCondition.class)
public class FreeloadMqConsumer
{
    @Resource
    private AdAppMapper adAppMapper;
    @Resource
    private AdLeagueMapper adLeagueMapper;
    @Resource
    private AdAppMediaMapper adAppMediaMapper;
    @Resource
    private CustomAdService customAdService;
    @Resource
    private AdClickMapper adClickMapper;

    @RabbitHandler
    public void receive(Object o) {
        FreeloadMqDTO freeloadMqDTO = getFreeloadMqDTO(o);
        if(null == freeloadMqDTO) {
            log.error("蹭量消息队列 消息异常：{}", JSON.toJSON(o));
            return ;
        }
        log.info("蹭量消息队列 接收消息：{}", JSON.toJSON(freeloadMqDTO));

        AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(freeloadMqDTO.getDestAppMediaId());
        AdApp adApp = adAppMapper.selectByPrimaryKey(adAppMedia.getAppId());
        AdLeague adLeague = adLeagueMapper.selectByPrimaryKey(adApp.getLeagueId());
        AdClick adClick = convertAdClick(freeloadMqDTO.getSourceAdClick(), adAppMedia, adLeague);

        // 保存点击记录
        adClickMapper.insertSelective(adClick);

        String reportUrl;
        try
        {
            reportUrl = customAdService.getUrl(adApp, adClick, adLeague);
        } catch (UnsupportedEncodingException e)
        {
            log.error("获取上报地址失败adClickId:{}", adClick.getId());
            return;
        }

        HttpResult httpResult = HttpUtils.get(reportUrl);
        log.info("蹭量上报地址：{} 返回值状态码:{}", reportUrl, httpResult.getStatus());
    }

    private FreeloadMqDTO getFreeloadMqDTO(Object o) {
        try
        {
            FreeloadMqDTO freeloadMqDTO =  null;
            Message message = null;
            if(o instanceof String) {
                freeloadMqDTO = JSONObject.parseObject((String) o, FreeloadMqDTO.class);
            } else if(o instanceof Message) {
                message = (Message) o;
                freeloadMqDTO = JSONObject.parseObject(new String(message.getBody()), FreeloadMqDTO.class);
            } else {
                log.warn("蹭量消息队列 消息为空");
                return null;
            }
            return freeloadMqDTO;
        } catch (Exception e)
        {
            log.error("蹭量消息队列 消息异常:{}", JSON.toJSON(o));
            return null;
        }
    }

    private AdClick convertAdClick(AdClick sourceAdClick, AdAppMedia adAppMedia, AdLeague adLeague) {
        int nowTime = (int) (System.currentTimeMillis()/1000);

        AdClick adClick = new AdClick();
        BeanUtils.copyProperties(adClick, sourceAdClick);
        adClick.setId(null);
        adClick.setAppMediaId(adAppMedia.getId());
        adClick.setAppId(adAppMedia.getAppId());
        adClick.setMediaId(adAppMedia.getMediaId());
        adClick.setLeagueId(adLeague.getId());
        adClick.setUpdateTime(nowTime);
        adClick.setCreateTime(nowTime);
        return adClick;
    }

}
