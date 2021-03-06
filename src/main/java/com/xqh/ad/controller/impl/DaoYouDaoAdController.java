package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.controller.api.IDaoYouDaoAdController;
import com.xqh.ad.entity.other.CallbackResponse;
import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.enums.CallbackResponseEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

/**
 * Created by hssh on 2017/8/31.
 */
@RestController
public class DaoYouDaoAdController implements IDaoYouDaoAdController
{
    private static Logger logger = LoggerFactory.getLogger(DaoYouDaoAdController.class);

    @Autowired
    private XQHAdService xqhAdService;

    @Override
    public void callback(HttpServletRequest req, HttpServletResponse resp)
    {
        TreeMap<String, String> params = CommonUtils.getParams(req);
        logger.info("道有道 callback params:{}", JSONObject.toJSON(params));

        String clickIdStr = req.getParameter("clickid");

        if(!StringUtils.isNumeric(clickIdStr))
        {
            logger.error("道有道异常 clickId{}不合法 ", clickIdStr);
            CommonUtils.writeResponse(resp, JSONObject.toJSONString(new CallbackResponse(CallbackResponseEnum.ERROR_CLICK_ID)));
            return ;
        }

        xqhAdService.callback(Integer.valueOf(clickIdStr), CommonUtils.getFullUrl(req));
        CommonUtils.writeResponse(resp, JSONObject.toJSONString(new CallbackResponse(CallbackResponseEnum.SUCC)));
        return;
    }
}
