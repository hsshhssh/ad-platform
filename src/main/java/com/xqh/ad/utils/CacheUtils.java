package com.xqh.ad.utils;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.entity.AdMedia;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.tkmapper.mapper.AdMediaMapper;
import com.xqh.ad.utils.common.SpringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by hssh on 2017/5/27.
 */
public class CacheUtils
{

    private static AdMediaMapper adMediaMapper = SpringUtils.getBean(AdMediaMapper.class);

    private static AdAppMapper adAppMapper = SpringUtils.getBean(AdAppMapper.class);

    private static AdLeagueMapper adLeagueMapper = SpringUtils.getBean(AdLeagueMapper.class);

    private static LoadingCache<Integer, String> adMediaIdToName = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<Integer, String>()
            {
                @Override
                public String load(Integer key) throws Exception
                {
                    AdMedia adMedia = adMediaMapper.selectByPrimaryKey(key);
                    if(adMedia != null)
                    {
                        return adMedia.getName();
                    }
                    else
                    {
                        return "";
                    }
                }
            });

    private static LoadingCache<Integer, String> adAppIdToName = CacheBuilder.newBuilder()
            .maximumSize(200)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<Integer, String>()
            {
                @Override
                public String load(Integer key) throws Exception
                {
                    AdApp adApp = adAppMapper.selectByPrimaryKey(key);

                    return null != adApp ? adApp.getName() : "";
                }
            });

    private static LoadingCache<Integer, String> adLeagueIdToName = CacheBuilder.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<Integer, String>()
            {
                @Override
                public String load(Integer key) throws Exception
                {
                    AdLeague adLeague = adLeagueMapper.selectByPrimaryKey(key);

                    return null != adLeague ? adLeague.getName() : "";
                }
            });

    /**
     * 通过media取得mediaName
     * @param id
     * @return
     */
    public static String getMediaNameById(int id)
    {
        String name = null;
        try
        {
            name = adMediaIdToName.get(id);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        if(StringUtils.isBlank(name))
        {
            adMediaIdToName.refresh(id);
        }

        return name;
    }

    /**
     * 通过appId取得appName
     * @param id
     * @return
     */
    public static String getAppNameById(int id)
    {
        String name = null;

        try
        {
            name = adAppIdToName.get(id);
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        if(StringUtils.isBlank(name))
        {
            adAppIdToName.refresh(id);
        }

        return name;
    }


    /**
     * 通过联盟id获取联盟名称
     */
    public static String getLeagueNameById(int id)
    {
        String name = null;

        try
        {
            name = adLeagueIdToName.get(id);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        if(StringUtils.isBlank(name))
        {
            adLeagueIdToName.refresh(id);
        }

        return name;
    }
}
