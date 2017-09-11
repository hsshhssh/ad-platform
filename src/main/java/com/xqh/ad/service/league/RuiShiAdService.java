package com.xqh.ad.service.league;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import com.xqh.ad.tkmapper.entity.AdRuishiInfo;
import com.xqh.ad.tkmapper.mapper.AdRuishiInfoMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.UrlUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/8/31.
 */
@Service
public class RuiShiAdService extends LeagueAbstractService
{
    private static Logger logger = LoggerFactory.getLogger(RuiShiAdService.class);

    @Autowired
    private AdRuishiInfoMapper ruishiInfoMapper;

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick)
    {
        logger.info("瑞狮url appid:{} url:{}", adApp.getId(), adApp.getLeagueUrl());

        try
        {
            String url = getUrl(adApp, adClick);
            if(StringUtils.isBlank(url))
            {
                throw new RuntimeException("获取url失败");
            }
            logger.info("调用瑞狮推广url:{}", url);
            resp.sendRedirect(url);
        }
        catch (IOException e)
        {
            logger.error("瑞狮推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;
        }
        catch (Exception e)
        {
            logger.error("瑞狮推广跳转异常 adClick:{} e:{}", adClick, Throwables.getStackTraceAsString(e));
            CommonUtils.writeResponse(resp, Constant.ERROR_SYS);
            return ;
        }

    }


    public String getUrl(AdApp adApp, AdClick adClick)
    {
        // 获得域名
        String host = UrlUtils.UrlPage(adApp.getLeagueUrl());
        Map<String, String> params = UrlUtils.URLRequest(adApp.getLeagueUrl());

        // 添加参数
        if(Constant.PHONE_TYPE_ANDROID == adClick.getPhoneType())
        {
            logger.info("瑞狮 andrond 未开发 clickId:{}", adClick.getId());
            return null;
        }
        else
        {
            logger.info("瑞狮广 ios clickId:{}", adClick.getId());
            params.put("idfa", adClick.getIdfa());
        }

        //TODO q配置化
        Search search = new Search();
        search.put("appId_eq", adApp.getId());
        Example example = new ExampleBuilder(AdRuishiInfo.class).search(search).build();
        List<AdRuishiInfo> adRuishiInfos = ruishiInfoMapper.selectByExample(example);
        if(adRuishiInfos.size() != 1)
        {
            logger.info("瑞狮推广 appId:{} 配置信息有误", adApp.getId());
            return null;
        }

        params.put("q", adRuishiInfos.get(0).getRuishiQ());
        params.put("clickid", String.valueOf(adClick.getId()));

        List<String> paramList = Lists.newArrayList();
        for (String s : params.keySet())
        {
            if(StringUtils.isNotBlank(params.get(s)))
            {
                paramList.add(s.trim() + "=" + params.get(s).trim());
            }
        }

        return host + "?" + Joiner.on("&").join(paramList);

    }
}
