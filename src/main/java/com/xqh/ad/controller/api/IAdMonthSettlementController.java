package com.xqh.ad.controller.api;

import com.xqh.ad.entity.dto.AdMonthSettlementCreateDTO;
import com.xqh.ad.entity.dto.AdMonthSettlementDeleteDTO;
import com.xqh.ad.entity.dto.AdMonthSettlementUpdateDTO;
import com.xqh.ad.entity.vo.AdMonthSettlementSearchVO;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2018/3/31.
 */
@RequestMapping("xqh/ad/month/settlement")
public interface IAdMonthSettlementController
{
    @PostMapping("list")
    public AdMonthSettlementSearchVO search(@RequestParam("search") Search search,
                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size);


    @PutMapping
    public int insert(@RequestBody @Valid @NotNull AdMonthSettlementCreateDTO dto,
                      HttpServletResponse resp);


    @PostMapping
    public int update(@RequestBody @Valid @NotNull AdMonthSettlementUpdateDTO dto);

    @PostMapping("delete")
    public int delete(@RequestBody @Valid @NotNull AdMonthSettlementDeleteDTO dto);

    @GetMapping("tempCreate")
    public void tempCreate(@RequestParam(value = "year") int year,
                           @RequestParam(value = "month") int month);

}
