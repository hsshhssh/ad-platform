package com.xqh.ad.controller.impl;

import com.github.pagehelper.Page;
import com.xqh.ad.controller.api.IAdAppController;
import com.xqh.ad.entity.dto.AdAppCreateDTO;
import com.xqh.ad.entity.dto.AdAppUpdateDTO;
import com.xqh.ad.entity.vo.AdAppVO;
import com.xqh.ad.exception.ErrorResponseEunm;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.enums.ReportTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * Created by hssh on 2017/9/10.
 */
@RestController
public class AdAppController implements IAdAppController
{
    private static Logger logger = LoggerFactory.getLogger(AdAppController.class);

    @Autowired
    private AdAppMapper adAppMapper;

    @Autowired
    private AdLeagueMapper adLeagueMapper;

    @Override
    public int insert(@RequestBody @Valid @NotNull AdAppCreateDTO dto, HttpServletResponse resp)
    {
        if(ReportTypeEnum._S2S.getValue() == dto.getReportType() && StringUtils.isBlank(dto.getRedirectUrl()))
        {
            logger.error("参数异常 dto:{}", dto);
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_S2S_PARAM);
            return 0;
        }

        AdApp adApp = DozerUtils.map(dto, AdApp.class);

        // 检验leagueId
        AdLeague adLeague = adLeagueMapper.selectByPrimaryKey(adApp.getLeagueId());
        if(null == adLeague)
        {
            logger.error("insert app, leagueId error dto:{}", dto);
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_LEAGUEID);
            return 0;
        }

        int nowTime = (int) (System.currentTimeMillis()/1000);
        adApp.setLeagueCode(adLeague.getCode());
        adApp.setCreateTime(nowTime);
        adApp.setUpdateTime(nowTime);
        adAppMapper.insertSelective(adApp);

        return adApp.getId();

    }

    @Override
    public int update(@RequestBody @Valid @NotNull AdAppUpdateDTO dto, HttpServletResponse resp)
    {
        if(ReportTypeEnum._S2S.getValue() == dto.getReportType() && StringUtils.isBlank(dto.getRedirectUrl()))
        {
            logger.error("参数异常 dto:{}", dto);
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_S2S_PARAM);
            return 0;
        }

        AdApp adApp = DozerUtils.map(dto, AdApp.class);

        adAppMapper.updateByPrimaryKeySelective(adApp);

        return 1;
    }

    @Override
    public PageResult<AdAppVO> search(@RequestParam("search") Search search,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Example example = new ExampleBuilder(AdApp.class).search(search).sort(Arrays.asList("id_desc")).build();

        Page<AdApp> appPage = (Page<AdApp>) adAppMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));

        return new PageResult<>(appPage.getTotal(), DozerUtils.mapList(appPage.getResult(), AdAppVO.class));
    }
}
