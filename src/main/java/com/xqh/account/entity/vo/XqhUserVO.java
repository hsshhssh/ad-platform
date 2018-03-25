package com.xqh.account.entity.vo;

import lombok.Data;

/**
 * Created by hssh on 2017/9/10.
 */
@Data
public class XqhUserVO
{
    private Integer id;
    private String username;
    private String name;
    private String phone;
    private Integer role;
    private Integer createTime;
    private Integer updateTime;

    private String roleStr;
}
