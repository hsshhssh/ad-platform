package com.xqh.ad.tkmapper.mapper;

import com.xqh.ad.tkmapper.entity.AdDownloadCount;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface AdDownloadCountMapper extends Mapper<AdDownloadCount>
{
    AdDownloadCount selectByAdAppMediaIdLock(@Param("adAppMediaId") Integer adAppMediaId);

}