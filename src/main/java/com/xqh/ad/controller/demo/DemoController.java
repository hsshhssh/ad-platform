package com.xqh.ad.controller.demo;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/8/12.
 */
@RestController
@RequestMapping("demo")
public class DemoController
{
    @GetMapping("tencent")
    public void tencent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String host = "http://ac.o2.qq.com/php/mbclick.php";

        Map<String, String> params = Maps.newHashMap();
        params.put("sign", "report");
        params.put("gid", "107415");
        params.put("media", "9689");
        params.put("ifa", "A89CF116-49C1-493A-9EBB-CB68AF567BAD");
        params.put("cip", "183.14.135.222");
        params.put("callback", URLEncoder.encode("http://139.196.51.152:8080/reyun/callback?id=1&app=2","UTF-8"));
        params.put("time", String.valueOf(System.currentTimeMillis()/1000));
        params.put("scid", "1");
        params.put("tagid", "阳仔");
        params.put("create_id", "create_id");

        List<String> paramList = Lists.newArrayList();
        for (String s : params.keySet())
        {
            paramList.add(s.trim() + "=" + params.get(s).trim());
        }

        String url = host + "?" + Joiner.on("&").join(paramList);

        String u = "https://itunes.apple.com.com/cn/app/id1186647303";

        resp.sendRedirect(url);
    }



}
