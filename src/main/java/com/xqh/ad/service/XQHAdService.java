package com.xqh.ad.service;

import com.xqh.ad.exception.NoLeagueChannelException;
import com.xqh.ad.service.league.*;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdDownload;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.tkmapper.mapper.AdDownloadMapper;
import com.xqh.ad.utils.AsyncUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private AdDownloadMapper adDownloadMapper;

    @Autowired
    private AsyncUtils asyncUtils;

    @Autowired
    private AdAppMediaMapper adAppMediaMapper;

    @Autowired
    private DaoYouDaoAdService daoYouDaoAdService;

    @Autowired
    private RuiShiAdService ruiShiAdService;

    @Autowired
    private CaulyAdService caulyAdService;

    @Autowired
    private VirtualAdService virtualAdService;

    /**
     * 根据联盟编码选择Service
     */
    public LeagueAbstractService dispatchLeague(String leagueCode)
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
            logger.error("通道异常 leagueCode:{}",  leagueCode);
            throw new NoLeagueChannelException();
        }

    }


    /**
     * 回调处理
     * @param clickId
     */
    public void callback(int clickId)
    {
        AdClick adClick = adClickMapper.selectByPrimaryKeyLock(clickId);

        if(null == adClick)
        {
            logger.error("广告平台回调异常 clickId{}不合法 ", clickId);
            return ;
        }

        // TODO 计算扣量
        boolean isSkip = isSkipDownLoad(adClick);

        logger.info("扣量结果 isSkip:{}", isSkip);

        // 插入下载记录
        int nowTime = (int) (System.currentTimeMillis()/1000);
        AdDownload adDownload = DozerUtils.map(adClick, AdDownload.class);
        adDownload.setIsSkip(isSkip ? 1 : 0);
        adDownload.setId(null);
        adDownload.setClickId(adClick.getId());
        adDownload.setCreateTime(nowTime);
        adDownload.setUpdateTime(nowTime);

        adDownloadMapper.insertSelective(adDownload);

        logger.info("广告平台 异步回调开始 clickId:{}", clickId);

        if(!isSkip)
        {
            logger.info("不扣量 回调下游 clickId:{}", clickId);
            asyncUtils.callbackUser(adClick.getCallbackUrl());
        }
        else
        {
            logger.info("扣量 不回调下游 clickId:{}", clickId);
        }

    }


    /**
     * 判断是否需要扣量
     * @param adClick
     * @return
     */
    public boolean isSkipDownLoad(AdClick adClick)
    {
        logger.info("判断是否需要扣量开始 clickId:{}", adClick.getId());

        AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(adClick.getAppMediaId());

        if(null == adAppMedia)
        {
            logger.error("计算扣量失败 正常回调 appMediaId异常 clickId:{} appMediaId:{}", adClick.getId(), adClick.getAppMediaId());
            return false;
        }

        // 计算该应用当前下载量
        Search search = new Search();
        search.put("appMediaId_eq", adClick.getAppMediaId());
        int curDownloadCount = adDownloadMapper.selectCountByExample(new ExampleBuilder(AdDownload.class).search(search).build());
        
        logger.info("clickId:{} appMediaId:{} 当前下载量:{} 扣量初始值:{} 扣量率:{}", adClick.getId(), adClick.getAppMediaId(), curDownloadCount, adAppMedia.getStartCount(), adAppMedia.getDiscountRate());
        
        if(curDownloadCount < adAppMedia.getStartCount())
        {
            logger.info("clickId:{} appMediaId:{} 当前下载量小于初始值 不扣量", adClick.getId(),  adClick.getAppMediaId());
            return  false;
        }


        // 取得差额的个位数
        int diff = curDownloadCount - adAppMedia.getStartCount() + 1;
        int unit = diff % 10;

        if(unit >= (adAppMedia.getDiscountRate() * 10))
        {
            logger.info("clickId:{} appMediaId:{} 扣量", adClick.getId(), adClick.getAppMediaId());
            return true;
        }
        else
        {
            logger.info("clickId:{} appMediaId:{} 不扣量", adClick.getId(), adClick.getAppMediaId());
            return false;
        }


    }


}
