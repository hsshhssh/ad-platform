package com.xqh.ad.service.tx;

import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdDownload;
import com.xqh.ad.tkmapper.entity.AdDownloadCount;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdDownloadCountMapper;
import com.xqh.ad.tkmapper.mapper.AdDownloadMapper;
import com.xqh.ad.utils.DiscountConfigUtils;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class AdDownloadTxService {

    @Resource
    private AdDownloadCountMapper adDownloadCountMapper;
    @Resource
    private AdAppMediaMapper adAppMediaMapper;
    @Resource
    private AdDownloadMapper adDownloadMapper;
    @Resource
    private DiscountConfigUtils discountConfigUtils;

    /**
     * 统计ad_app_media下载次数并插入下载记录
     * @return 返回是否扣量
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean countAndInsertDdDownload(AdClick adClick)
    {
        log.info("#############clickId:{}###########start", adClick.getId());
        // 加锁 保证同一个adAppMediaId同步进行
        AdDownloadCount adDownloadCount = getAdDownloadCountByAppMediaId(adClick.getAppMediaId());
        log.info("#############clickId:{}###########get lock", adClick.getId());

        // 计算当前adAppMedia下载量
        int currentDownloadCount = getAppMediaIdCurrentDownloadCount(adClick.getAppMediaId());

        // 判断是否需要扣量
        boolean isSkip = isSkipDownLoad(adClick, currentDownloadCount);
        log.info("扣量结果 isSkip:{}", isSkip);

        // 插入下载记录
        int nowTime = (int) (System.currentTimeMillis()/1000);
        AdDownload adDownload = DozerUtils.map(adClick, AdDownload.class);
        adDownload.setIsSkip(isSkip ? 1 : 0);
        adDownload.setId(null);
        adDownload.setClickId(adClick.getId());
        adDownload.setCreateTime(nowTime);
        adDownload.setUpdateTime(nowTime);
        adDownloadMapper.insertSelective(adDownload);

        // 更新下载次数
        AdDownloadCount newRecord = new AdDownloadCount();
        newRecord.setId(adDownloadCount.getId());
        newRecord.setDownloadCount(currentDownloadCount + 1);
        adDownloadCountMapper.updateByPrimaryKeySelective(newRecord);

        log.info("#############clickId:{}###########end downloadCount:{}", adClick.getId(), newRecord.getDownloadCount());
        return isSkip;
    }


    private AdDownloadCount getAdDownloadCountByAppMediaId(Integer appMediaId)
    {
        Search search = new Search();
        search.put("appMediaId_eq", appMediaId);
        List<AdDownloadCount> adDownloadCountList = adDownloadCountMapper.selectByExample(new ExampleBuilder(AdDownloadCount.class).search(search).build());
        if(CollectionUtils.isEmpty(adDownloadCountList))
        {
            AdDownloadCount record = initAdDownloadCount(appMediaId);
            try
            {
                adDownloadCountMapper.insertSelective(record);
            }
            catch (DuplicateKeyException e)
            {
                log.info("初始化adDownloadCount时并发 appMediaId:{}", appMediaId);
                return adDownloadCountMapper.selectByAdAppMediaIdLock(appMediaId);
            }

            return record;
        }
        else
        {
            return adDownloadCountMapper.selectByAdAppMediaIdLock(appMediaId);
        }
    }

    private AdDownloadCount initAdDownloadCount(Integer appMediaId)
    {
        AdDownloadCount adDownloadCount = new AdDownloadCount();
        adDownloadCount.setAppMediaId(appMediaId);
        adDownloadCount.setDownloadCount(0);
        return adDownloadCount;
    }


    /**
     * 获取推广应用当前下载量
     */
    private int getAppMediaIdCurrentDownloadCount(Integer appMediaId)
    {
        Search search = new Search();
        search.put("appMediaId_eq", appMediaId);
        return adDownloadMapper.selectCountByExample(new ExampleBuilder(AdDownload.class).search(search).build());
    }

    /**
     * 判断是否需要扣量
     * @param adClick
     * @return
     */
    private boolean isSkipDownLoad(AdClick adClick, int curDownloadCount)
    {
        log.info("判断是否需要扣量开始 clickId:{}", adClick.getId());

        AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(adClick.getAppMediaId());

        if(null == adAppMedia)
        {
            log.error("计算扣量失败 正常回调 appMediaId异常 clickId:{} appMediaId:{}", adClick.getId(), adClick.getAppMediaId());
            return false;
        }


        log.info("clickId:{} appMediaId:{} 当前下载量:{} 扣量初始值:{} 扣量率:{}", adClick.getId(), adClick.getAppMediaId(), curDownloadCount, adAppMedia.getStartCount(), adAppMedia.getDiscountRate());

        if(curDownloadCount < adAppMedia.getStartCount())
        {
            log.info("clickId:{} appMediaId:{} 当前下载量小于初始值 不扣量", adClick.getId(),  adClick.getAppMediaId());
            return  false;
        }


        // 取得差额的个位数
        int diff = curDownloadCount - adAppMedia.getStartCount() + 1;
        int unit = diff % 10;

        String discountConfig = discountConfigUtils.getConfig(adAppMedia.getDiscountRate());
        log.info("discountConfig:{}", discountConfig);
        if(StringUtils.isNotBlank(discountConfig) && discountConfig.length() == 10)
        {
            log.info("扣量计算zk配置化 回调率：{} 配置：{}", adAppMedia.getDiscountRate(), discountConfig);
            char flag = discountConfig.charAt(unit);
            if(java.util.Objects.equals('1', flag))
            {
                log.info("第：{}位标识：1 回调 不扣量", unit);
                return false;
            }
            else
            {
                log.info("第：{}为标识：非1 不回调 扣量 flag:{}", unit, flag);
                return true;
            }
        }
        else
        {
            log.info("扣量计算db配置化 回调率：{}", adAppMedia.getDiscountRate());
            if(unit >= (adAppMedia.getDiscountRate() * 10))
            {
                log.info("clickId:{} appMediaId:{} 扣量", adClick.getId(), adClick.getAppMediaId());
                return true;
            }
            else
            {
                log.info("clickId:{} appMediaId:{} 不扣量", adClick.getId(), adClick.getAppMediaId());
                return false;
            }
        }


    }

}
