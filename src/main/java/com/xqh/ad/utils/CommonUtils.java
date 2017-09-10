package com.xqh.ad.utils;

import com.xqh.ad.exception.ErrorResponseEunm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
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

    /**
     * 取得零点时间
     */
    public static int getZeroHourTime(int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        return (int) (date.getTime()/1000);
    }

    /**
     * 取得指定位数随机数
     */
    public static String getRandom(int num)
    {
        String chars = "0123456789";
        char[] rands = new char[num];

        for(int i=0; i<num; i++)
        {
            int rand = (int)(Math.random() * 10);
            rands[i] = chars.charAt(rand);
        }

        return String.valueOf(rands);
    }

    /**
     * 返回错误信息
     */
    public static void sendError(HttpServletResponse resp, ErrorResponseEunm errorResponseEunm)
    {
        try
        {
            resp.sendError(errorResponseEunm.status, errorResponseEunm.msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * md5加密
     * @param plainText
     * @return
     */
    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

}
