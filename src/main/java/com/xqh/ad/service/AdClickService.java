package com.xqh.ad.service;

import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.mapper.AdClickMapper;
import com.xqh.ad.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * Created by hssh on 2017/9/8.
 */
@Service
public class AdClickService
{
    public static Logger logger = LoggerFactory.getLogger(AdClickService.class);

    @Autowired
    private AdClickMapper adClickMapper;

    public String insert(AdClick adClick)
    {
        if(null == adClick.getAppMediaId() || adClick.getAppMediaId() <= 0)
        {
            throw new IllegalArgumentException("insert AdClick appMediaId为空");
        }

        // 设置创建时间
        long nowTime = System.currentTimeMillis();
        adClick.setCreateTime((int) (nowTime/1000));

        // 计算id
        adClick.setId(getAdClickId(nowTime, adClick.getAppMediaId()));

        try
        {
            adClickMapper.insertSelective(adClick);
        }
        catch (DuplicateKeyException e)
        {
            logger.info("插入点击表 记录重复 id:{} 重试一次", adClick.getId());
            adClick.setId(getAdClickId(nowTime, adClick.getAppMediaId()));

            adClickMapper.insertSelective(adClick);

        }

        return adClick.getId();
    }


    /**
     * 计算点击表id : 13位时间戳 + 5位appMediaId + 5位随机数
     */
    private String getAdClickId(long nowTime, int appMediaId)
    {
        String appMediaIdStr = String.format("%05d", appMediaId);

        String rand = CommonUtils.getRandom(5);

        return nowTime + appMediaIdStr + rand;

    }


}
