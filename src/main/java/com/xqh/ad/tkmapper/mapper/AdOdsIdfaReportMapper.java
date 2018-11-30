package com.xqh.ad.tkmapper.mapper;

import com.xqh.ad.tkmapper.entity.AdOdsIdfaReport;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdOdsIdfaReportMapper extends Mapper<AdOdsIdfaReport>
{

    List<AdOdsIdfaReport> selectByRecordAndLimitRaw(@Param("record") AdOdsIdfaReport record, @Param("count") Integer count);

    List<AdOdsIdfaReport> selectByRecordAndLimit(@Param("tableIndex") String tableIndex, @Param("record") AdOdsIdfaReport record, @Param("count") Integer count);

    int deleteById(@Param("tableIndex") String tableIndex, @Param("id") Integer id);

    int updateByIds(@Param("tableIndex") String tableIndex, @Param("record") AdOdsIdfaReport record, @Param("ids") List<Integer> ids);
}