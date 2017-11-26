package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.xqh.ad.controller.api.ICustomAdController;
import com.xqh.ad.entity.other.CallbackResponse;
import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.service.league.CustomAdService;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.entity.AdLeagueReportConfig;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.tkmapper.mapper.AdLeagueReportConfigMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.Constant;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.constant.CallbackResponseEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by hssh on 2017/9/17.
 */
@RestController
public class CustomAdController implements ICustomAdController
{
    private static Logger logger = LoggerFactory.getLogger(CustomAdController.class);

    @Autowired
    private AdLeagueMapper adLeagueMapper;

    @Autowired
    private AdLeagueReportConfigMapper configMapper;

    @Autowired
    private XQHAdService xqhAdService;

    @Autowired
    private CustomAdService customAdService;

    @Override
    public void callback(HttpServletRequest req,
                         HttpServletResponse resp,
                         @PathVariable("enName") String enName)
    {
        // 查询联盟信息
        Search searchLeague = new Search();
        searchLeague.put("enName_eq", enName);
        List<AdLeague> adLeagueList = adLeagueMapper.selectByExample(new ExampleBuilder(AdLeague.class).search(searchLeague).build());

        if(adLeagueList.size() != 1)
        {
            logger.error("enName:{} 无效 ", enName);
            return ;
        }

        AdLeague adLeague = adLeagueList.get(0);


        // 获取clickId key值
        Search searchConfig = new Search();
        searchConfig.put("leagueId_eq", adLeague.getId());
        searchConfig.put("xqhKey_eq", Constant.CALLBACK_CLICK_ID);
        List<AdLeagueReportConfig> configList = configMapper.selectByExample(new ExampleBuilder(AdLeagueReportConfig.class).search(searchConfig).sort(Arrays.asList("id_desc")).build());

        if(configList.size() != 1)
        {
            logger.warn("联盟 :{} callbackClickId配置信息异常 size：{}", adLeague.getName(), configList.size());
        }

        String callbackClickIdKey = configList.get(0).getLeagueKey();
        logger.info("联盟 ：{} 回调clickid key值：{}", adLeague.getName(), callbackClickIdKey);



        // 添加下载记录并回调
        TreeMap<String, String> params = CommonUtils.getParams(req);
        logger.info("联盟 ：{} callback params:{}", adLeague.getName(), JSONObject.toJSON(params));

        String clickIdStr = req.getParameter(callbackClickIdKey);


        // 特殊处理
        clickIdStr = dealSpecialProcess(enName, clickIdStr);

        if(!StringUtils.isNumeric(clickIdStr))
        {
            logger.error("联盟 ：{}  clickId{}不合法 ", adLeague.getName(), clickIdStr);
            CommonUtils.writeResponse(resp, JSONObject.toJSONString(new CallbackResponse(CallbackResponseEnum.ERROR_CLICK_ID)));
            return ;
        }

        xqhAdService.callback(Integer.valueOf(clickIdStr));
        CommonUtils.writeResponse(resp, JSONObject.toJSONString(new CallbackResponse(CallbackResponseEnum.SUCC)));
        return;

    }


    private String dealSpecialProcess(String leagueEnName, String clickIdStr)
    {
        // mac参数带上clickId
        if(customAdService.isMacWithClickIdLeague(leagueEnName))
        {
            List<String> macClickIdList = Splitter.on(Constant.MAC_CLICKID_SEPARATE).trimResults().omitEmptyStrings().splitToList(clickIdStr);
            return macClickIdList.get(macClickIdList.size() - 1);
        }

        return clickIdStr;
    }

}
