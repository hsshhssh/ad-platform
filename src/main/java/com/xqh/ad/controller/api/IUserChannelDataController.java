package com.xqh.ad.controller.api;

import com.xqh.ad.entity.dto.UserChannelDataCreateDTO;
import com.xqh.ad.entity.dto.UserChannelDataDeleteDTO;
import com.xqh.ad.entity.dto.UserChannelDataUpdateDTO;
import com.xqh.ad.entity.vo.UserChannelDataSearchVO;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2018/3/25.
 */
@RequestMapping("xqh/ad/user/channel/data")
public interface IUserChannelDataController
{

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public UserChannelDataSearchVO search(@RequestParam("search") Search search,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size);


    @PutMapping
    public int insert(@RequestBody @Valid @NotNull UserChannelDataCreateDTO dto,
                      HttpServletResponse resp);


    @PostMapping
    public int update(@RequestBody @Valid @NotNull UserChannelDataUpdateDTO dto,
                      HttpServletResponse resp);

    @PostMapping(value = "delete")
    public int delete(@RequestBody @Valid @NotNull UserChannelDataDeleteDTO dto,
                      HttpServletResponse resp);
}
