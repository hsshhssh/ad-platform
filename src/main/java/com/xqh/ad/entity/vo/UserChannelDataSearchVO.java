package com.xqh.ad.entity.vo;

import com.xqh.ad.utils.common.PageResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hssh on 2018/3/29.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChannelDataSearchVO
{
    /**
     * 列表数据（包含分页）
     */
    private PageResult<UserChannelDataVO> pageVO;

    /**
     * 汇总数据
      */
    private UserChannelDataVO totalVO;

}
