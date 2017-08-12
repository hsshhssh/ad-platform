package com.xqh.ad.controller.demo;

import com.xqh.ad.entity.vo.AdAppVO;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import org.hssh.common.zkconf.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by hssh on 2017/8/12.
 */
@RestController
public class DemoController
{
    @Autowired
    private AdAppMapper adAppMapper;

    @Value(path = "/config/zkconf/ad/xqh_ad", key = "test")
    private String test;

    @GetMapping("get")
    public String queryOne()
    {
        //Search search = new Search();
        //search.put("name_eq", "1");
        //Example example = new ExampleBuilder(AdApp.class).search(search).build();
        //
        //List<AdApp> adApps = adAppMapper.selectByExample(example);

        //return DozerUtils.map(adApps.get(0), AdAppVO.class);

        return test;
    }

}
