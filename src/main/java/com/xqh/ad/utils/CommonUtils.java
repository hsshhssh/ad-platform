package com.xqh.ad.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hssh on 2017/5/1.
 */
public class CommonUtils {

    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);


    /**
     * 返回
     */
    public static void writeResponse(HttpServletResponse resp, Object object) {
        try {
            resp.getWriter().print(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得请求参数
     * @param request
     * @return
     */
    public static TreeMap<String, String> getParams(HttpServletRequest request){
        TreeMap<String, String> map = new TreeMap<String, String>();
        Map reqMap = request.getParameterMap();
        for(Object key:reqMap.keySet()){
            String value = ((String[])reqMap.get(key))[0];
            //System.out.println(key+";"+value);
            map.put(key.toString(),value);
        }
        return map;
    }
}
