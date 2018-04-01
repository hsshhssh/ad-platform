package com.xqh.ad.entity.vo;

import com.xqh.ad.utils.common.PageResult;
import lombok.Data;

/**
 * Created by hssh on 2018/4/1.
 */
@Data
public class AdMonthSettlementSearchVO
{
    /**
     * 列表数据（包含分页）
     */
    private PageResult<AdMonthSettlementVO> pageVO;

    /**
     * 汇总数据
     */
    private AdMonthSettlementVO totalVO;
}
