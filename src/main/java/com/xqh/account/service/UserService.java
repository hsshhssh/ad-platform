package com.xqh.account.service;

import com.google.common.collect.Lists;
import com.xqh.account.entity.vo.UserInfoVO;
import com.xqh.account.tkmapper.entity.XqhUser;
import com.xqh.account.tkmapper.entity.XqhUserRole;
import com.xqh.account.tkmapper.mapper.XqhUserMapper;
import com.xqh.account.tkmapper.mapper.XqhUserRoleMapper;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hssh on 2017/9/10.
 */
@Service
public class UserService
{

    @Autowired
    private XqhUserRoleMapper xqhUserRoleMapper;
    @Resource
    private XqhUserMapper xqhUserMapper;

    /**
     * 获得用户信息对象
     * @param xqhUser
     * @return
     */
    public UserInfoVO genUserInfoVOByPayUser(XqhUser xqhUser)
    {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setAvatar("https://wdl.wallstreetcn.com/48a3e1e0-ea2c-4a4e-9928-247645e3428b");
        userInfoVO.setIntroduction(xqhUser.getName());
        userInfoVO.setName(xqhUser.getName());

        // 获取角色
        Search search = new Search();
        search.put("userId_eq", xqhUser.getId());
        Example example = new ExampleBuilder(XqhUserRole.class).search(search).build();

        List<XqhUserRole> xqhUserRoleList = xqhUserRoleMapper.selectByExample(example);

        List<String> roleList = Lists.newArrayList();
        for (XqhUserRole xqhUserRole : xqhUserRoleList)
        {
            roleList.add(xqhUserRole.getRoleName());
        }

        userInfoVO.setRole(roleList);


        userInfoVO.setToken(xqhUser.getUsername());
        userInfoVO.setUid(xqhUser.getId());

        return userInfoVO;

    }

    public XqhUser getUserById(int id)
    {
        Search search = new Search();
        search.put("id_eq", id);
        Example example = new ExampleBuilder(XqhUser.class).search(search).build();

        List<XqhUser> xqhUserList = xqhUserMapper.selectByExample(example);

        return xqhUserList.size() > 0 ? xqhUserList.get(0) : null;
    }

}
