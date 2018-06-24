package com.xqh.ad.tkmapper.mapper;

import com.xqh.ad.tkmapper.entity.AdOdsIdfaReport;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdOdsIdfaReportMapper extends Mapper<AdOdsIdfaReport>
{

    List<AdOdsIdfaReport> selectByRecordAndLimit(@Param("record") AdOdsIdfaReport record, @Param("count") Integer count);

}