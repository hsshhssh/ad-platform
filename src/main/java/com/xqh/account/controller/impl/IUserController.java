package com.xqh.account.controller.impl;

import com.xqh.account.entity.dto.XqhUserCreateDTO;
import com.xqh.account.entity.dto.XqhUserUpdateDTO;
import com.xqh.account.entity.vo.UserInfoVO;
import com.xqh.account.entity.vo.XqhUserVO;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created by hssh on 2017/9/10.
 */
@RequestMapping("/xqh/ad/account/")
public interface IUserController
{

    @PutMapping
    public int insert(@RequestBody @Valid XqhUserCreateDTO user,
                      HttpServletResponse resp);

    @PostMapping
    public int update(@RequestBody @Valid XqhUserUpdateDTO user);

    @PostMapping("list")
    public PageResult<XqhUserVO> queryList(@RequestParam("search") @NotNull Search search,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") @Max(1000) Integer size);


    @PostMapping("/login")
    public UserInfoVO login(@RequestParam(value = "userName") String userName,
                            @RequestParam(value = "password") String password,
                            HttpServletResponse resp);


    @GetMapping("/info")
    public UserInfoVO info(@RequestParam(value = "token") String token);

    @PostMapping("/reset")
    public int reset(@RequestParam(value = "userName") String userName,
                     @RequestParam(value = "passwordOld") String passwordOld,
                     @RequestParam(value = "password") String password,
                     HttpServletResponse resp);


}
