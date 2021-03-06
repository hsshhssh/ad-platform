package com.xqh.ad.controller.impl;

import com.github.pagehelper.Page;
import com.xqh.ad.controller.api.IAdLeagueController;
import com.xqh.ad.entity.dto.AdLeagueCreateDTO;
import com.xqh.ad.entity.dto.AdLeagueReportConfigCreateDTO;
import com.xqh.ad.entity.dto.AdLeagueReportConfigUpdateDTO;
import com.xqh.ad.entity.dto.AdLeagueUpdateDTO;
import com.xqh.ad.entity.vo.AdLeagueVO;
import com.xqh.ad.exception.ErrorResponseEunm;
import com.xqh.ad.service.AdLeagueService;
import com.xqh.ad.tkmapper.entity.AdLeague;
import com.xqh.ad.tkmapper.mapper.AdLeagueMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
public class AdLeagueController implements IAdLeagueController
{
    private static Logger logger = LoggerFactory.getLogger(AdLeagueController.class);

    @Autowired
    private AdLeagueMapper adLeagueMapper;

    @Autowired
    private AdLeagueService adLeagueService;

    @Override
    public PageResult<AdLeagueVO> search(@RequestParam("search") Search search,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Example example = new ExampleBuilder(AdLeague.class).search(search).sort(Arrays.asList("id_desc")).build();

        Page<AdLeague> appPage = (Page<AdLeague>) adLeagueMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));

        return new PageResult<>(appPage.getTotal(), DozerUtils.mapList(appPage.getResult(), AdLeagueVO.class));
    }

    @Override
    public int insert(@RequestBody @Valid @NotNull AdLeagueCreateDTO dto,
                      HttpServletResponse resp)
    {
        AdLeague adLeague = DozerUtils.map(dto, AdLeague.class);

        int id = 0;
        try
        {
            id = adLeagueService.insert(adLeague);
        } catch (DuplicateKeyException e)
        {
            logger.info("联盟英文名重复");
            CommonUtils.sendError(resp, ErrorResponseEunm.DUPLICATE_LEAGUE_EN_NAME);
            return 0;
        }

        return id;
    }

    @Override
    public int update(@RequestBody @Valid @NotNull AdLeagueUpdateDTO dto,
                      HttpServletResponse resp)
    {
        AdLeague adLeague = DozerUtils.map(dto, AdLeague.class);

        try
        {
            adLeagueService.update(adLeague);
        } catch (DuplicateKeyException e)
        {

            logger.info("联盟英文名重复");
            CommonUtils.sendError(resp, ErrorResponseEunm.DUPLICATE_LEAGUE_EN_NAME);
            return 0;
        }

        return 1;
    }

    @Override
    public int insertReportConfig(@RequestBody @Valid @NotNull AdLeagueReportConfigCreateDTO dto)
    {
        return 0;
    }

    @Override
    public int updateReportConfing(@RequestBody @Valid @NotNull AdLeagueReportConfigUpdateDTO dto)
    {
        return 0;
    }

    @Override
    public int deleteReportConfig(@RequestParam("id") Integer id)
    {
        return 0;
    }
}
