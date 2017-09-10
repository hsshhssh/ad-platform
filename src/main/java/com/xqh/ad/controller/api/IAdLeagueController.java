package com.xqh.ad.controller.api;

import com.xqh.ad.entity.vo.AdLeagueVO;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
