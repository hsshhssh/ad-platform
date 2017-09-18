package com.xqh.ad.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hssh on 2017/9/11.
 */
@RequestMapping("/xqh/ad/cauly")
public interface ICaulyAdController
{

    @GetMapping("callback")
    public void callback(HttpServletRequest req, HttpServletResponse resp);

}
