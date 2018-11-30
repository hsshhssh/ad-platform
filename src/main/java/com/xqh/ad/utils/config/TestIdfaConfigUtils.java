package com.xqh.ad.utils.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.xqh.ad.entity.vo.AdTestIdfaVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.hssh.common.zkconf.ValueWithMethod;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * 测试idfa配置类
 * Created by hssh on 2018/5/5.
 */
@Component
@Slf4j
public class TestIdfaConfigUtils
{
    private static ImmutableList<AdTestIdfaVO> TEST_IDFA_LIST;

    @ValueWithMethod(path = "/config/zkconf/ad_test_idfa_conf")
    public void adTestIdfaHandler(PropertiesConfiguration properties)
    {
        List<AdTestIdfaVO> tempList = Lists.newArrayList();
        Iterator<String> keys = properties.getKeys();
        String key;
        while (keys.hasNext())
        {
            key = keys.next();
            AdTestIdfaVO testIdfaVO = new AdTestIdfaVO();
            testIdfaVO.setName(key.trim());
            testIdfaVO.setValue(properties.getString(key));
            tempList.add(testIdfaVO);
        }

        log.info("测试idfa列表配置刷新前list:{}", JSON.toJSON(TEST_IDFA_LIST));
        log.info("测试idfa列表配置刷新后list:{}", JSON.toJSON(tempList));
        TEST_IDFA_LIST = ImmutableList.copyOf(tempList);
    }

    public static List<AdTestIdfaVO> getList()
    {
        return TEST_IDFA_LIST;
    }


}
