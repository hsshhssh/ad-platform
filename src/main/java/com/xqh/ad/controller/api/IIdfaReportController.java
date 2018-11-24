package com.xqh.ad.controller.api;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/xqh/ad/idfa/report")
public interface IIdfaReportController {

    @PostMapping("copy/record")
    public String copyRecord(@RequestBody JSONObject jsonObject);
}
