package com.xqh.ad.service;

import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by hssh on 2017/8/12.
 */
@Service
public class AdAppMediaService
{

    @Autowired
    private AdAppMediaMapper adAppMediaMapper;

    /**
     * 生成推广url编码
     */
    public String getUrlCode(int appId, int mediaId)
    {
        return String.format("%05d%05d", appId, mediaId);
    }

    /**
     * 通过推广链接编码获得信息
     */
    public AdAppMedia getByUrlCode(String urlCode)
    {
        Search search = new Search();
        search.put("urlCode_eq", urlCode);
        Example example = new ExampleBuilder(AdAppMedia.class).search(search).build();

        List<AdAppMedia> adAppMediaList = adAppMediaMapper.selectByExample(example);

        if(adAppMediaList.size() == 0)
        {
            return null;
        }
        else
        {
            return adAppMediaList.get(0);
        }
    }

}
