package com.xqh.ad.entity.vo;

import com.xqh.ad.utils.common.PageResult;
import lombok.Data;

/**
 * 上报页面VO
 * Created by hssh on 2018/5/5.
 */
@Data
public class TestReportVO
{
    /**
     * 测试idfa列表
     */
    private PageResult<AdTestIdfaVO>  idfaPageResult;

    /**
     * 默认ip
     */
    private String defalutIP;

    /**
     * 推广游戏列表
     */
    private PageResult<AdAppMediaVO> appMediaPageResult;

}
