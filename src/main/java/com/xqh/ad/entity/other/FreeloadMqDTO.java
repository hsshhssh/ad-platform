package com.xqh.ad.entity.other;

import com.xqh.ad.tkmapper.entity.AdClick;
import lombok.Data;

/**
 * Created by hssh on 2018/5/11.
 */
@Data
public class FreeloadMqDTO
{

    /**
     * 被蹭量推广应用id
     */
    private Integer sourceAppMediaId;

    /**
     * 蹭量推广应用id
     */
    private Integer destAppMediaId;


    /**
     * 点击记录
     */
    private AdClick sourceAdClick;
}
