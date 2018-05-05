package com.xqh.ad.controller.api;

import com.xqh.ad.entity.dto.AdAppMediaCreateDTO;
import com.xqh.ad.entity.dto.AdAppMediaUpdateDTO;
import com.xqh.ad.entity.dto.TestReportUrlDTO;
import com.xqh.ad.entity.vo.AdAppMediaVO;
import com.xqh.ad.entity.vo.TestReportUrlVO;
import com.xqh.ad.entity.vo.TestReportVO;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@RequestMapping("xqh/ad/appMedia")
public interface IAdAppMediaController
{
    @PutMapping
    public int insert(@RequestBody @Valid @NotNull AdAppMediaCreateDTO dto,
                      HttpServletResponse resp);


    @PostMapping
    public int update(@RequestBody @Valid @NotNull AdAppMediaUpdateDTO dto,
                      HttpServletResponse resp);

    @RequestMapping("search")
    public PageResult<AdAppMediaVO> search(@RequestParam("search") Search search,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size);

    @GetMapping("test/url")
    public String getTestUrl(@RequestParam("id") int id, HttpServletResponse resp);

    @GetMapping("test/report/data")
    public TestReportVO getTestReportData();

    @PostMapping("test/report/url")
    public TestReportUrlVO getTestReportUrl(@RequestBody @Valid @NotNull TestReportUrlDTO dto,
                                      HttpServletResponse resp);

}
