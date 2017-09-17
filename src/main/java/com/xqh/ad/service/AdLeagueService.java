package com.xqh.ad.service;

import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hssh on 2017/9/17.
 */
@Service
public class AdLeagueService
{
    private static Logger logger = LoggerFactory.getLogger(AdLeagueService.class);

    @Autowired
    private AdLeagueMapper adLeagueMapper;

    @Transactional
    public int insert(AdLeague adLeague)
    {
        int nowTime = (int) (System.currentTimeMillis()/1000);

        adLeague.setCallbackUrl(getCallbackUrl(adLeague.getEnName()));
        adLeague.setUpdateTime(nowTime);
        adLeague.setCreateTime(nowTime);

        int id = adLeagueMapper.insertSelective(adLeague);


        adLeague.setCode(String.format("%04d", id));

        adLeagueMapper.updateByPrimaryKeySelective(adLeague);

        return id;

    }

    public void update(AdLeague adLeague)
    {
        int nowTime = (int) (System.currentTimeMillis()/1000);

        adLeague.setCallbackUrl(adLeague.getEnName());
        adLeague.setUpdateTime(nowTime);

        adLeagueMapper.updateByPrimaryKeySelective(adLeague);


    }

    private String getCallbackUrl(String enName)
    {
        return Constant.BASCURL + "/xqh/ad/custom/" + enName + "/callback";
    }
}
