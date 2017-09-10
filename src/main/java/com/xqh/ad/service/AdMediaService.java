package com.xqh.ad.service;

import com.xqh.ad.tkmapper.entity.AdMedia;
import com.xqh.ad.tkmapper.mapper.AdMediaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hssh on 2017/9/10.
 */
@Service
public class AdMediaService
{
    private static Logger logger = LoggerFactory.getLogger(AdMediaService.class);

    @Autowired
    private AdMediaMapper adMediaMapper;

    @Transactional
    public int insert(AdMedia adMedia)
    {
        int nowTime = (int) (System.currentTimeMillis()/1000);
        adMedia.setUpdateTime(nowTime);
        adMedia.setCreateTime(nowTime);

        adMediaMapper.insertSelective(adMedia);

        adMedia.setCode(String.format("%04d", adMedia.getId()));

        adMediaMapper.updateByPrimaryKeySelective(adMedia);


        return adMedia.getId();
    }

}
