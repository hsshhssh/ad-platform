package com.xqh.ad.service;

import com.xqh.ad.entity.other.PPMediaParam;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.PPMediaConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by hssh on 2017/10/2.
 */
@Service
public class AdPPMediaService
{

    private static Logger logger = LoggerFactory.getLogger(AdPPMediaService.class);

    public void addPPMediaParam(Map<String, String> params, Integer appMediaId, String idfa)
    {
        PPMediaParam ppMediaParam = PPMediaConfigUtils.paramMap.get(appMediaId);
        if(null == ppMediaParam)
        {
            throw new RuntimeException("pp助手配置参数不存在 appMediaId:" + appMediaId);
        }

        logger.info("pp助手配置前参数 param:{}", params);

        params.put("cid", ppMediaParam.getCid());
        params.put("appid", ppMediaParam.getAppid());
        params.put("sign", getSign(ppMediaParam.getCid(), ppMediaParam.getCkey(), ppMediaParam.getAppid(), idfa));

        logger.info("pp助手配置后参数 param:{}", params);
    }

    private String getSign(String cid, String ckey, String appid, String idfa)
    {
        return CommonUtils.getMd5(cid + ckey + appid + idfa);
    }

}
