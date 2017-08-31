package com.xqh.ad.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hssh on 2017/8/31.
 */
@RequestMapping("/xqh/ad/ruishi")
public interface IRuiShiAdController
{
    @GetMapping("callback")
    public void callback(HttpServletRequest req, HttpServletResponse resp);
}
