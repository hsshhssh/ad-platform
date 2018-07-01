package com.xqh.ad.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.xqh.ad.exception.ErrorResponseEunm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hssh on 2017/5/1.
 */
@Slf4j
public class CommonUtils {


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
     * 拼接url
     */
    public static String getFullUrl(String host, Map<String, String> params)
    {
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

    public static String getFullUrl(HttpServletRequest request)
    {
        String host = request.getRequestURI();
        TreeMap<String, String> params = getParams(request);
        return getFullUrl(host, params);
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

    /**
     * 取得某月的其实时间和结束时间
     */
    public static List<Integer> getMonthStartEndTime(int month, int year )
    {
        List<Integer> res = Lists.newArrayList();

        if(month <= 1)
        {
            month = 1;
        } else if(month >= 12)
        {
            month = 12;
        }

        if(month < 12 )
        {
            month -= 1;
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            res.add((int) (cal.getTime().getTime()/1000));

            cal.add(Calendar.MONTH, 1);
            res.add((int) (cal.getTime().getTime()/1000));
            return res;
        }
        else
        {
            // month == 12
            Calendar cal = Calendar.getInstance();
            cal.set(year, 11, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            res.add((int) (cal.getTime().getTime()/1000));


            cal.add(Calendar.YEAR, 1);
            cal.set(Calendar.MONTH, 0);
            res.add((int) (cal.getTime().getTime()/1000));
        }
        return res;
    }

    public static List<Integer> getLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        return Arrays.asList(year, month);

    }

    public static String getFormatDate(String format)
    {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }


    public static void main(String[] args)
    {
        getLastMonth();
    }

}
