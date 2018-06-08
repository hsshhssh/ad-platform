package com.xqh.ad.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.entity.other.IdfaReportMqDTO;
import com.xqh.ad.mq.RabbitConfig;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.utils.AsyncUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.condition.IdfaReportMqConsumerCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by hssh on 2018/6/6.
 */
@Component
@RabbitListener(queues = RabbitConfig.IDFA_REPORT_MQ)
@Slf4j
@Conditional(IdfaReportMqConsumerCondition.class)
public class IdfaReportMqConsumer extends AbstractMqConsumer<IdfaReportMqDTO>
{
    @Resource
    private AdAppMediaMapper adAppMediaMapper;
    @Resource
    private AdAppMapper adAppMapper;
    @Resource
    private AdLeagueMapper adLeagueMapper;
    @Resource
    private AsyncUtils asyncUtils;

    @RabbitHandler
    public void receive(Object o)
    {
        // 获取传输对象
        IdfaReportMqDTO idfaReportMqDTO = super.getMqDTO(o);
        if(null == idfaReportMqDTO)
        {
            log.error("idfa上报消费者 获取传输对象为空 o:{}", JSONObject.toJSON(o));
            return ;
        }

        // 校验appMediaId appId leagueId
        AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(idfaReportMqDTO.getAppMediaId());
        if(null == adAppMedia)
        {
            log.error("idfa上报消费者 appMediaId无效 idfaReportMqDTO:{}", JSONObject.toJSON(idfaReportMqDTO));
            return;
        }
        AdApp adApp = adAppMapper.selectByPrimaryKey(adAppMedia.getAppId());
        if(null == adApp)
        {
            log.error("idfa上报消费者 appId无效 appMediaId:{} idfaReportMqDTO:{}", idfaReportMqDTO.getAppMediaId(), JSONObject.toJSON(idfaReportMqDTO));
            return ;
        }
        AdLeague adLeague = adLeagueMapper.selectByPrimaryKey(adApp.getLeagueId());
        if(null == adLeague)
        {
            log.error("idfa上报消费者 leagueId无效 appId:{} idfaReportMqDTO:{}", adApp.getLeagueUrl(), JSONObject.toJSON(idfaReportMqDTO));
            return ;
        }


        // 构造点击对象
        AdClick adClick = getAdClick(idfaReportMqDTO, adAppMedia, adApp);


        // 上报并删除idfa上报记录
        asyncUtils.idfaReportAndSaveAdClick(idfaReportMqDTO, adApp, adLeague, adClick);
    }


    private AdClick getAdClick(IdfaReportMqDTO reportMqDTO, AdAppMedia adAppMedia, AdApp adApp)
    {
        int nowTime = (int) (System.currentTimeMillis()/1000);
        AdClick adClick = new AdClick();
        adClick.setAppMediaId(reportMqDTO.getAppMediaId());
        adClick.setAppId(adAppMedia.getAppId());
        adClick.setMediaId(adAppMedia.getMediaId());
        adClick.setLeagueId(adApp.getLeagueId());
        adClick.setPhoneType(Constant.PHONE_TYPE_IOS);
        adClick.setIp(reportMqDTO.getIp());
        adClick.setIdfa(reportMqDTO.getIdfa());
        adClick.setUpdateTime(nowTime);
        adClick.setCreateTime(nowTime);
        return adClick;
    }


}
