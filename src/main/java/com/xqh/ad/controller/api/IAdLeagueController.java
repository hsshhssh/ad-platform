package com.xqh.ad.controller.api;

import com.xqh.ad.entity.dto.AdLeagueCreateDTO;
import com.xqh.ad.entity.dto.AdLeagueReportConfigCreateDTO;
import com.xqh.ad.entity.dto.AdLeagueReportConfigUpdateDTO;
import com.xqh.ad.entity.dto.AdLeagueUpdateDTO;
import com.xqh.ad.entity.vo.AdLeagueVO;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@RequestMapping("xqh/ad/league")
public interface IAdLeagueController
{

    @PostMapping("search")
    public PageResult<AdLeagueVO> search(@RequestParam("search") Search search,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size);

    @PutMapping
    public int insert(@RequestBody @Valid @NotNull AdLeagueCreateDTO dto,
                      HttpServletResponse resp);

    @PostMapping
    public int update(@RequestBody @Valid @NotNull AdLeagueUpdateDTO dto,
                      HttpServletResponse resp);

    @PutMapping("reportConfig")
    public int insertReportConfig(@RequestBody @Valid @NotNull AdLeagueReportConfigCreateDTO dto);

    @PostMapping("reportConfig")
    public int updateReportConfing(@RequestBody @Valid @NotNull AdLeagueReportConfigUpdateDTO dto);

    @DeleteMapping("reportConfig")
    public int deleteReportConfig(@RequestParam("id") Integer id);

}
