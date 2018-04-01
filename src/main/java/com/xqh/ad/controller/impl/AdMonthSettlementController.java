package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.xqh.ad.controller.api.IAdMonthSettlementController;
import com.xqh.ad.entity.dto.AdMonthSettlementCreateDTO;
import com.xqh.ad.entity.dto.AdMonthSettlementDeleteDTO;
import com.xqh.ad.entity.dto.AdMonthSettlementUpdateDTO;
import com.xqh.ad.entity.vo.AdMonthSettlementSearchVO;
import com.xqh.ad.entity.vo.AdMonthSettlementVO;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdMonthSettlement;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdMonthSettlementMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.DoubleUtils;
import com.xqh.ad.utils.MonthSettlementJobs;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import com.xqh.ad.utils.constant.MonthSettlementSourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by hssh on 2018/3/31.
 */
@RestController
@Slf4j
public class AdMonthSettlementController implements IAdMonthSettlementController
{
    @Resource
    private AdMonthSettlementMapper adMonthSettlementMapper;
    @Resource
    private AdAppMediaMapper adAppMediaMapper;
    @Resource
    private AdAppMapper adAppMapper;
    @Resource
    private MonthSettlementJobs monthSettlementJobs;

    @Override
    public AdMonthSettlementSearchVO search(@RequestParam("search") Search search,
                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        AdMonthSettlementSearchVO searchVO = new AdMonthSettlementSearchVO();

        PageResult<AdMonthSettlementVO> pageResult = getPageVO(search, page, size);
        searchVO.setPageVO(pageResult);

        AdMonthSettlementVO totalVO = getTotalVO(pageResult.getList());
        searchVO.setTotalVO(totalVO);

        return searchVO;
    }

    private AdMonthSettlementVO getTotalVO(List<AdMonthSettlementVO> list) {
        AdMonthSettlementVO totalVO = new AdMonthSettlementVO();
        int totalDownload = 0, skipDownload = 0, callbackDownload = 0;
        int settlementSkip = 0, settlemtCallback = 0;
        double diffProfit = 0.00d, skipProfit = 0.00d, totalProfit = 0.00d;
        for (AdMonthSettlementVO monthSettlementVO : list)
        {
            totalDownload += monthSettlementVO.getTotalDownload();
            skipDownload += monthSettlementVO.getSkipDownload();
            callbackDownload += monthSettlementVO.getCallbackDownload();
            settlementSkip += monthSettlementVO.getSettlementSkip();
            settlemtCallback += monthSettlementVO.getSettlementCallback();
            diffProfit = DoubleUtils.add(diffProfit, monthSettlementVO.getDiffProfit());
            skipProfit = DoubleUtils.add(skipProfit, monthSettlementVO.getSkipProfit());
            totalProfit = DoubleUtils.add(totalProfit, monthSettlementVO.getTotalProfit());
        }

        totalVO.setTotalDownload(totalDownload);
        totalVO.setSkipDownload(skipDownload);
        totalVO.setCallbackDownload(callbackDownload);
        totalVO.setSettlementSkip(settlementSkip);
        totalVO.setSettlementCallback(settlemtCallback);
        totalVO.setDiffProfit(diffProfit);
        totalVO.setSkipProfit(skipProfit);
        totalVO.setTotalProfit(totalProfit);
        return totalVO;
    }

    private PageResult<AdMonthSettlementVO> getPageVO(Search search, int page, int size) {
        Example example = new ExampleBuilder(AdMonthSettlement.class).search(search).sort(Arrays.asList("id_desc")).build();
        Page<AdMonthSettlement> list = (Page<AdMonthSettlement>) adMonthSettlementMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));
        return new PageResult<>(list.getTotal(), DozerUtils.mapList(list, AdMonthSettlementVO.class));
    }

    @Override
    public int insert(@RequestBody @Valid @NotNull AdMonthSettlementCreateDTO dto, HttpServletResponse resp)
    {
        AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(dto.getAdAppMediaId());
        AdApp adApp = adAppMapper.selectByPrimaryKey(adAppMedia.getAppId());

        AdMonthSettlement adMonthSettlement = new AdMonthSettlement();
        adMonthSettlement.setSource(MonthSettlementSourceEnum.MANUAL.getCode());
        adMonthSettlement.setPutTime(dto.getPutTime());
        adMonthSettlement.setMediaId(adAppMedia.getMediaId());
        adMonthSettlement.setLeagueId(adApp.getLeagueId());
        adMonthSettlement.setAppId(adApp.getId());
        return adMonthSettlementMapper.insertSelective(adMonthSettlement);
    }

    @Override
    public int update(@RequestBody @Valid @NotNull AdMonthSettlementUpdateDTO dto)
    {
        AdMonthSettlement adMonthSettlement = adMonthSettlementMapper.selectByPrimaryKey(dto.getId());
        adMonthSettlement.setSettlementTime(dto.getSettlementTime());
        adMonthSettlement.setSettlementSkip(dto.getSettlementSkip());
        adMonthSettlement.setSettlementCallback(dto.getSettlementCallback());
        adMonthSettlement.setUpstreamPrice(dto.getUpstreamPrice());
        adMonthSettlement.setDownstreamPrice(dto.getDownstreamPrice());
        adMonthSettlement.setIsConfirm(1);

        // 计算差价利润
        double diff = DoubleUtils.sub(dto.getDownstreamPrice(), dto.getUpstreamPrice());
        double diffProfit = DoubleUtils.mul(diff, dto.getSettlementCallback());
        // 计算扣量利润
        double skipProfit = DoubleUtils.mul(dto.getSettlementSkip(), dto.getDownstreamPrice());
        // 计算总利润
        double totalProfit = DoubleUtils.add(diffProfit, skipProfit);

        adMonthSettlement.setDiffProfit(diffProfit);
        adMonthSettlement.setSkipProfit(skipProfit);
        adMonthSettlement.setTotalProfit(totalProfit);
        adMonthSettlement.setUpdateTime((int) (System.currentTimeMillis()/1000));

        return adMonthSettlementMapper.updateByPrimaryKeySelective(adMonthSettlement);
    }

    @Override
    public int delete(@RequestBody @Valid @NotNull AdMonthSettlementDeleteDTO dto)
    {
        AdMonthSettlement adMonthSettlement = adMonthSettlementMapper.selectByPrimaryKey(dto.getId());
        if(!Objects.equals(adMonthSettlement.getSource(), MonthSettlementSourceEnum.MANUAL.getCode())) {
            log.error("非手工生成的记录不能删除 dto={}", JSON.toJSON(dto));
            return 0;
        }

        return adMonthSettlementMapper.deleteByPrimaryKey(dto.getId());
    }

    @Override
    public void tempCreate(@RequestParam(value = "year") int year, @RequestParam(value = "month") int month)
    {
        List<Integer> startAndEndTime = CommonUtils.getMonthStartEndTime(month, year);
        List<AdMonthSettlement> monthSettlementList = monthSettlementJobs.getMonthSettlementByTime(startAndEndTime.get(0), startAndEndTime.get(1));
        log.info("年份={} 月份={} 开始时间={} 结束时间={}", year, month, startAndEndTime.get(0), startAndEndTime.get(1));

        for (AdMonthSettlement monthSettlement : monthSettlementList)
        {
            adMonthSettlementMapper.insertSelective(monthSettlement);
        }

    }
}
