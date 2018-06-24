package com.xqh.ad.service;

import com.google.common.base.Throwables;
import com.xqh.ad.entity.other.FreeloadMqDTO;
import com.xqh.ad.exception.NoLeagueChannelException;
import com.xqh.ad.mq.producer.FreeloadMqProducer;
import com.xqh.ad.service.league.*;
import com.xqh.ad.service.tx.AdDownloadTxService;
import com.xqh.ad.tkmapper.entity.*;
import com.xqh.ad.tkmapper.mapper.*;
import com.xqh.ad.utils.*;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.constant.MediaTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/8/12.
 */
@Service
public class XQHAdService
{
    private static Logger logger = LoggerFactory.getLogger(XQHAdService.class);

    @Autowired
    private ReYunAdService reYunAdService;
    @Autowired
    private TencentAdService tencentAdService;
    @Autowired
    private YouMengAdService youMengAdService;
    @Autowired
    private AdClickMapper adClickMapper;
    @Autowired
    private AsyncUtils asyncUtils;
    @Autowired
    private DaoYouDaoAdService daoYouDaoAdService;
    @Autowired
    private RuiShiAdService ruiShiAdService;
    @Autowired
    private CaulyAdService caulyAdService;
    @Autowired
    private VirtualAdService virtualAdService;
    @Autowired
    private CustomAdService customAdService;
    @Autowired
    private AdMediaMapper adMediaMapper;
    @Autowired
    private AdMediaCallbackConfigMapper adMediaCallbackConfigMapper;
    @Autowired
    private ConfigUtils configUtils;
    @Autowired
    private AdPPMediaService adPPMediaService;
    @Resource
    private AdFreeloadRelateMapper adFreeloadRelateMapper;
    @Resource
    private FreeloadMqProducer freeloadMqProducer;
    @Resource
    private AdClickHistoryMapper adClickHistoryMapper;
    @Resource
    private AdDownloadMissRecordMapper adDownloadMissRecordMapper;
    @Resource
    private AdDownloadTxService adDownloadTxService;

    /**
     * 根据联盟编码选择Service
     */
    public LeagueAbstractService dispatchLeague(String leagueCode, int leagueId)
    {
        if(Constant.REYUN.equals(leagueCode))
        {
            logger.info("热云通道");
            return reYunAdService;
        }
        else if(Constant.TENCENT.equals(leagueCode))
        {
            logger.info("腾讯通道");
            return tencentAdService;
        }
        else if(Constant.YOUMENG.equals(leagueCode))
        {
            logger.info("友盟通道");
            return youMengAdService;
        }
        else if(Constant.DAOYOUDAO.equals(leagueCode))
        {
            logger.info("道有道通道");
            return daoYouDaoAdService;
        }
        else if(Constant.RUISHI.equals(leagueCode))
        {
            logger.info("瑞狮通道");
            return ruiShiAdService;
        }
        else if(Constant.CAULY.equals(leagueCode))
        {
            logger.info("CAULY通道");
            return caulyAdService;
        }
        else if(Constant.VIRTUAL.equals(leagueCode))
        {
            logger.info("虚拟通道");
            return virtualAdService;
        }
        else
        {
            if(customAdService.isCustomLeague(leagueId))
            {
                logger.info("自定义配置联盟");
                return customAdService;
            }

            logger.error("通道异常 leagueCode:{}",  leagueCode);
            throw new NoLeagueChannelException();
        }

    }

    /**
     * 回调处理
     * @param clickId
     */
    public void callback(int clickId, String mediaCallbackUrl)
    {
        AdClick adClick = getAdClickByCallback(clickId, mediaCallbackUrl);

        if(null == adClick)
        {
            logger.error("广告平台回调异常 clickId{}不合法 ", clickId);
            return ;
        }

        // 保存点击记录并返回是否需要扣量
        boolean isSkip = adDownloadTxService.countAndInsertDdDownload(adClick);

        logger.info("广告平台 异步回调开始 clickId:{}", clickId);

        if(!isSkip)
        {

            String callbackUrl;
            try
            {
                callbackUrl = getCallbackUrl(adClick);
            } catch (Exception e)
            {
                logger.info("获取回调地址异常" + Throwables.getStackTraceAsString(e));
                return ;
            }

            logger.info("不扣量 回调下游 clickId:{}", clickId);
            asyncUtils.callbackUser(callbackUrl);
        }
        else
        {
            logger.info("扣量 不回调下游 clickId:{}", clickId);
        }

    }

    /**
     * 回调时获得点击记录
     */
    private AdClick getAdClickByCallback(int clickId, String mediaCallbackUrl)
    {
        AdClick adClick = adClickMapper.selectByPrimaryKey(clickId);
        if(adClick != null)
        {
            logger.info("回调时获取点击记录 当日点击记录表命中 clickid:{}", clickId);
            return adClick;
        }
        else
        {
            adClick = new AdClick();
            AdClickHistory adClickHistory = adClickHistoryMapper.selectByPrimaryKey(clickId);
            if(adClickHistory != null)
            {
                logger.info("回调时获取点击记录 7日点击记录表命中 clickid:{}", clickId);
                BeanUtils.copyProperties(adClickHistory, adClick);
                return adClick;
            }
            else
            {
                int nowTime = (int) (System.currentTimeMillis()/1000);
                logger.info("回调时获取点击记录 7日点击记录表不命中 保存为回调丢失记录 clickid:{}", clickId);
                AdDownloadMissRecord adDownloadMissRecord = new AdDownloadMissRecord();
                adDownloadMissRecord.setMissClickId(clickId);
                adDownloadMissRecord.setMediaCallbackUrl(mediaCallbackUrl);
                adDownloadMissRecord.setCreateTime(nowTime);
                adDownloadMissRecord.setUpdateTime(nowTime);
                adDownloadMissRecordMapper.insertSelective(adDownloadMissRecord);
                return null;
            }
        }
    }


    private String getCallbackUrl(AdClick adClick)
    {
        AdMedia adMedia = adMediaMapper.selectByPrimaryKey(adClick.getMediaId());

        if(MediaTypeEnum.CUSTOM_CALLBACK.getValue() == adMedia.getType())
        {
            logger.info("自定义回调 mediaId:{} callbackUrl:{}", adMedia.getId(), adClick.getCallbackUrl());
            return adClick.getCallbackUrl();
        }
        else if(MediaTypeEnum.CONFIG_CALLBACK.getValue() == adMedia.getType())
        {
            logger.info("配置回调 mediaId:{}", adMedia.getId());

            Search search = new Search();
            search.put("mediaId_eq", adClick.getMediaId());
            List<AdMediaCallbackConfig> configList = adMediaCallbackConfigMapper.selectByExample(new ExampleBuilder(AdMediaCallbackConfig.class).search(search).build());

            String baseHost = UrlUtils.UrlPage(adMedia.getCallbackUrl());
            Map<String, String> params = UrlUtils.URLRequest(adMedia.getCallbackUrl());

            for (AdMediaCallbackConfig config : configList)
            {
                params.put(config.getMediaKey(), getValueByReflect(adClick, config.getXqhKey()));
            }

            if(configUtils.getPpMediaCode().equals(adMedia.getCode()))
            {
                adPPMediaService.addPPMediaParam(params, adClick.getAppMediaId(), adClick.getIdfa());
            }

            return CommonUtils.getFullUrl(baseHost, params);

        }

        throw new RuntimeException("参数异常");
    }

    private String getValueByReflect(AdClick adClick, String key)
    {
        String xqhKeyValue;
        Class<?> clazz = adClick.getClass();
        Field field;
        try
        {
            field = clazz.getDeclaredField(key);
            field.setAccessible(true);
            xqhKeyValue = String.valueOf(field.get(adClick));
        }
        catch (NoSuchFieldException e)
        {
            logger.info("配置下游 配置信息有误 key:{} e:{}", key, Throwables.getStackTraceAsString(e));

            throw new RuntimeException("配置信息有误");
        }
        catch (IllegalAccessException e)
        {
            logger.info("配置下游 配置信息有误 key:{} e:{}", key, Throwables.getStackTraceAsString(e));

            throw new RuntimeException("配置信息有误");
        }

        return xqhKeyValue;
    }



    /**
     * 蹭量处理：发送蹭量消息
     */
    public boolean handleFreeload(AdClick adClick, Integer sourceAppMediaId) {
        Search search = new Search();
        search.put("sourceAppMediaId_eq", sourceAppMediaId);
        Example example = new ExampleBuilder(AdFreeloadRelate.class).search(search).sort(Arrays.asList("id_desc")).build();
        List<AdFreeloadRelate> adFreeloadRelateList = adFreeloadRelateMapper.selectByExample(example);
        logger.info("蹭量消息数量：{}", adFreeloadRelateList.size());
        for (AdFreeloadRelate freeloadRelate : adFreeloadRelateList)
        {
            FreeloadMqDTO freeloadMqDTO = new FreeloadMqDTO();
            freeloadMqDTO.setSourceAppMediaId(sourceAppMediaId);
            freeloadMqDTO.setDestAppMediaId(freeloadRelate.getDestAppMediaId());
            freeloadMqDTO.setSourceAdClick(adClick);
            freeloadMqProducer.send(freeloadMqDTO);
        }
        return true;
    }

}
