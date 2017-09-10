package com.xqh.ad.controller.api;

import com.xqh.ad.entity.dto.AdMediaCreateDTO;
import com.xqh.ad.entity.dto.AdMediaUpdateDTO;
import com.xqh.ad.entity.vo.AdMediaVO;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@RequestMapping("xqh/ad/media")
public interface IAdMediaController
{
    @PutMapping
    public int insert(@RequestBody @Valid @NotNull AdMediaCreateDTO dto,
                      HttpServletResponse resp);


    @PostMapping
    public int update(@RequestBody @Valid @NotNull AdMediaUpdateDTO dto);


    @PostMapping("search")
    public PageResult<AdMediaVO> search(@RequestParam("search") Search search,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size);
}
