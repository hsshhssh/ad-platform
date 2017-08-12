package com.xqh.ad.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hssh on 2017/8/12.
 */
@RequestMapping("/xqh/ad/")
public interface IXQHAdController
{
    /**
     * 推广地址
     * @param req
     * @param resp
     */
    @GetMapping("{urlCode}")
    public void url(HttpServletRequest req,
                    HttpServletResponse resp,
                    @PathVariable("urlCode") String urlCode);

}
