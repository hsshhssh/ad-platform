package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.xqh.account.service.UserService;
import com.xqh.account.tkmapper.entity.XqhUser;
import com.xqh.ad.controller.api.IUserChannelDataController;
import com.xqh.ad.entity.dto.UserChannelDataCreateDTO;
import com.xqh.ad.entity.dto.UserChannelDataDeleteDTO;
import com.xqh.ad.entity.dto.UserChannelDataUpdateDTO;
import com.xqh.ad.entity.vo.UserChannelDataVO;
import com.xqh.ad.exception.ErrorResponseEunm;
import com.xqh.ad.tkmapper.entity.UserChannelData;
import com.xqh.ad.tkmapper.mapper.UserChannelDataMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * Created by hssh on 2018/3/25.
 */
@RestController
@Slf4j
public class UserChannelDataController implements IUserChannelDataController
{

    @Resource
    private UserChannelDataMapper userChannelDataMapper;
    @Resource
    private UserService userService;

    @Override
    public PageResult<UserChannelDataVO> search(@RequestParam("search") Search search,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size)
    {

        Example example = new ExampleBuilder(UserChannelData.class).search(search).sort(Arrays.asList("statisticsDate_desc")).build();
        Page<UserChannelData> dataPage = (Page<UserChannelData>) userChannelDataMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));
        return new PageResult<>(dataPage.getTotal(), DozerUtils.mapList(dataPage.getResult(), UserChannelDataVO.class));
    }

    @Override
    public int insert(@RequestBody @Valid @NotNull UserChannelDataCreateDTO dto, HttpServletResponse resp)
    {
        XqhUser user = userService.getUserById(dto.getChannelId());
        if (null == user)
        {
            log.error("用户id有误 dto:{}", JSON.toJSON(dto));
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_USER);
            return 0;
        }

        UserChannelData userChannelData = DozerUtils.map(dto, UserChannelData.class);
        userChannelData.setUserId(user.getId());
        userChannelData.setChannelName(user.getName());

        return userChannelDataMapper.insertSelective(userChannelData);
    }

    @Override
    public int update(@RequestBody @Valid @NotNull UserChannelDataUpdateDTO dto, HttpServletResponse resp)
    {
        UserChannelData userChannelData = userChannelDataMapper.selectByPrimaryKey(dto.getId());
        if (null == userChannelData)
        {
            log.error("id有误 dto:{}", JSON.toJSON(dto));
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_USER);
            return 0;
        }

        XqhUser user = userService.getUserById(dto.getChannelId());
        if (null == user)
        {
            log.error("用户id有误 dto:{}", JSON.toJSON(dto));
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_USER);
            return 0;
        }

        UserChannelData updateUserChannelData = DozerUtils.map(dto, UserChannelData.class);
        updateUserChannelData.setUserId(user.getId());
        updateUserChannelData.setChannelName(user.getName());
        updateUserChannelData.setId(userChannelData.getId());

        return userChannelDataMapper.updateByPrimaryKeySelective(updateUserChannelData);
    }

    @Override
    public int delete(@RequestBody @Valid @NotNull UserChannelDataDeleteDTO dto, HttpServletResponse resp)
    {
        return userChannelDataMapper.deleteByPrimaryKey(dto.getId());
    }
}
