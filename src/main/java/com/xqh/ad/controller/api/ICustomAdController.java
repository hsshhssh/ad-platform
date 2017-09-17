package com.xqh.ad.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hssh on 2017/9/17.
 */
@RequestMapping("/xqh/ad/custom")
public interface ICustomAdController
{
    @GetMapping("{enName}/callback")
    public void callback(HttpServletRequest req,
                         HttpServletResponse resp,
                         @PathVariable("enName") String enName);


}
