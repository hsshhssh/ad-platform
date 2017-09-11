package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSONObject;
import com.xqh.ad.controller.api.IVirtualAdController;
import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

/**
 * Created by hssh on 2017/9/11.
 */
@RestController
public class VirtualAdController implements IVirtualAdController
{
    private static Logger logger = LoggerFactory.getLogger(VirtualAdController.class);

    @Autowired
    private XQHAdService xqhAdService;

    @Override
    public void callback(HttpServletRequest req, HttpServletResponse resp)
    {
        TreeMap<String, String> params = CommonUtils.getParams(req);
        logger.info("虚拟联盟 callback params:{}", JSONObject.toJSON(params));

        String clickIdStr = req.getParameter("clickid");

        if(!StringUtils.isNumeric(clickIdStr))
        {
            logger.error("虚拟联盟 clickId{}不合法 ", clickIdStr);
            return ;
        }

        xqhAdService.callback(Integer.valueOf(clickIdStr));

    }
}
