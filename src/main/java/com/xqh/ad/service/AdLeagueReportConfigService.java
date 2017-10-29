package com.xqh.ad.service;

import com.xqh.ad.tkmapper.entity.AdLeagueReportConfig;
import com.xqh.ad.tkmapper.mapper.AdLeagueReportConfigMapper;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hssh on 2017/10/29.
 */
@Service
public class AdLeagueReportConfigService
{
    @Autowired
    private AdLeagueReportConfigMapper adLeagueReportConfigMapper;

    public List<AdLeagueReportConfig> getByLeagueIdWithSort(int leagueId)
    {
        Search searchConfig = new Search();
        searchConfig.put("leagueId_eq", leagueId);
        Example example = new ExampleBuilder(AdLeagueReportConfig.class)
                .search(searchConfig)
                .sort(Arrays.asList("weight_asc"))
                .build();

        return adLeagueReportConfigMapper.selectByExample(example);

    }

}
