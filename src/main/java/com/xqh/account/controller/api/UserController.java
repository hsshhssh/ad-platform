package com.xqh.account.controller.api;

import com.github.pagehelper.Page;
import com.xqh.account.controller.impl.IUserController;
import com.xqh.account.entity.dto.XqhUserCreateDTO;
import com.xqh.account.entity.dto.XqhUserUpdateDTO;
import com.xqh.account.entity.vo.UserInfoVO;
import com.xqh.account.entity.vo.XqhUserVO;
import com.xqh.account.service.UserService;
import com.xqh.account.tkmapper.entity.XqhUser;
import com.xqh.account.tkmapper.mapper.XqhUserMapper;
import com.xqh.ad.exception.ErrorResponseEunm;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hssh on 2017/9/10.
 */
@RestController
public class UserController implements IUserController
{
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private XqhUserMapper xqhUserMapper;

    @Autowired
    private UserService userService;

    @Override
    public int insert(@RequestBody @Valid XqhUserCreateDTO user, HttpServletResponse resp)
    {
        return 0;
    }

    @Override
    public int update(@RequestBody @Valid XqhUserUpdateDTO user) {
        return 0;
    }

    @Override
    public PageResult<XqhUserVO> queryList(@RequestParam("search") @NotNull Search search,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") @Max(1000) Integer size)
    {
        Example example = new ExampleBuilder(XqhUser.class).search(search).sort(Arrays.asList("id_desc")).build();

        Page<XqhUser> payUserList = (Page<XqhUser>) xqhUserMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));

        return new PageResult<>(payUserList.getTotal(), DozerUtils.mapList(payUserList, XqhUserVO.class));
    }

    @Override
    public UserInfoVO login(@RequestParam(value = "userName") String userName,
                            @RequestParam(value = "password") String password,
                            HttpServletResponse resp)
    {
        Search search = new Search();
        search.put("username_eq", userName);
        Example example = new ExampleBuilder(XqhUser.class).search(search).build();

        List<XqhUser> xqhUserList = xqhUserMapper.selectByExample(example);

        if(xqhUserList.size() != 1)
        {
            logger.info("登录失败 用户不存在 userName:{}", userName);
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_USER);
            return null;
        }


        XqhUser xqhUser = xqhUserList.get(0);
        if(!CommonUtils.getMd5(password).equals(xqhUser.getPassword()))
        {
            logger.warn("登录失败 密码错误 useName:{}, password:{}", userName, password);
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_PASSWORD);
            return null;
        }

        // 登录成功 返回用户信息
        return userService.genUserInfoVOByPayUser(xqhUser);
    }

    @Override
    public UserInfoVO info(@RequestParam(value = "token") String token)
    {
        Search search = new Search();
        search.put("username_eq", token);
        Example example = new ExampleBuilder(XqhUser.class).search(search).build();

        List<XqhUser> xqhUserList = xqhUserMapper.selectByExample(example);

        return userService.genUserInfoVOByPayUser(xqhUserList.get(0));
    }

    @Override
    public int reset(@RequestParam(value = "userName") String userName,
                     @RequestParam(value = "passwordOld") String passwordOld,
                     @RequestParam(value = "password") String password, HttpServletResponse resp)
    {
        Search search = new Search();
        search.put("username_eq", userName);
        Example example = new ExampleBuilder(XqhUser.class).search(search).build();

        List<XqhUser> payUserList = xqhUserMapper.selectByExample(example);

        if(payUserList.size() != 1)
        {
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_USER);
            return 0;
        }

        XqhUser payUser = payUserList.get(0);
        if(!CommonUtils.getMd5(passwordOld).equals(payUser.getPassword()))
        {
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_PASSWORD);
            return 0;
        }

        // 重置密码
        XqhUser record = new XqhUser();
        record.setId(payUser.getId());
        record.setPassword(CommonUtils.getMd5(password));
        xqhUserMapper.updateByPrimaryKeySelective(record);

        return 1;
    }


}
