package com.xqh.ad.controller.impl;

import com.github.pagehelper.Page;
import com.xqh.ad.controller.api.IAdLeagueController;
import com.xqh.ad.entity.vo.AdLeagueVO;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;

/**
 * Created by hssh on 2017/9/10.
 */
@RestController
public class AdLeagueController implements IAdLeagueController
{
    private static Logger logger = LoggerFactory.getLogger(AdLeagueController.class);

    @Autowired
    private AdLeagueMapper adLeagueMapper;

    @Override
    public PageResult<AdLeagueVO> search(@RequestParam("search") Search search,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Example example = new ExampleBuilder(AdLeague.class).search(search).sort(Arrays.asList("id_desc")).build();

        Page<AdLeague> appPage = (Page<AdLeague>) adLeagueMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));

        return new PageResult<>(appPage.getTotal(), DozerUtils.mapList(appPage.getResult(), AdLeagueVO.class));
    }
}
