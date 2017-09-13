package com.xqh.ad.controller.api;

import com.xqh.ad.entity.dto.AdAppCreateDTO;
import com.xqh.ad.entity.dto.AdAppUpdateDTO;
import com.xqh.ad.entity.vo.AdAppVO;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/9.
 */
@RequestMapping("xqh/ad/app")
public interface IAdAppController
{
    @PutMapping
    public int insert(@RequestBody @Valid @NotNull AdAppCreateDTO dto,
                      HttpServletResponse resp);


    @PostMapping
    public int update(@RequestBody @Valid @NotNull AdAppUpdateDTO dto,
                      HttpServletResponse resp);


    @PostMapping("search")
    public PageResult<AdAppVO> search(@RequestParam("search") Search search,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size);




}
