package com.xqh.ad.controller.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import com.xqh.ad.controller.api.IAdAppMediaController;
import com.xqh.ad.entity.dto.AdAppMediaCreateDTO;
import com.xqh.ad.entity.dto.AdAppMediaUpdateDTO;
import com.xqh.ad.entity.vo.AdAppMediaVO;
import com.xqh.ad.exception.ErrorResponseEunm;
import com.xqh.ad.service.AdAppMediaService;
import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdMedia;
import com.xqh.ad.tkmapper.mapper.AdAppMapper;
import com.xqh.ad.tkmapper.mapper.AdAppMediaMapper;
import com.xqh.ad.tkmapper.mapper.AdMediaMapper;
import com.xqh.ad.utils.CommonUtils;
import com.xqh.ad.utils.ConfigUtils;
import com.xqh.ad.utils.TestParamConfigUtils;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by hssh on 2017/9/10.
 */
@RestController
public class AdAppMediaController implements IAdAppMediaController
{
    private static Logger logger = LoggerFactory.getLogger(AdAppMediaController.class);

    @Autowired
    private AdAppMediaMapper adAppMediaMapper;

    @Autowired
    private AdAppMapper adAppMapper;

    @Autowired
    private AdMediaMapper adMediaMapper;

    @Autowired
    private AdAppMediaService adAppMediaService;

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private TestParamConfigUtils testParamConfigUtils;

    @Override
    public int insert(@RequestBody @Valid @NotNull AdAppMediaCreateDTO dto,
                      HttpServletResponse resp)
    {
        AdAppMedia adAppMedia = DozerUtils.map(dto, AdAppMedia.class);

        // 检验appId mediaId
        AdApp adApp = adAppMapper.selectByPrimaryKey(adAppMedia.getAppId());
        AdMedia adMedia = adMediaMapper.selectByPrimaryKey(adAppMedia.getMediaId());
        if(null == adApp || null == adMedia)
        {
            logger.error("insert addMedia, appId or mediaId error, dto：{}", dto);
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_APPID_MEDIAID);
            return 0;
        }

        // 校验扣量初始值 回调率
        ErrorResponseEunm errorResponseEunm = validateStartCountAndDiscountRate(dto.getStartCount(), dto.getDiscountRate());
        if (null != errorResponseEunm) {
            logger.error("校验不通过 req:{}", JSON.toJSON(dto));
            CommonUtils.sendError(resp, errorResponseEunm);
            return 0;
        }

        adAppMedia.setMediaCode(adMedia.getCode());
        adAppMedia.setUrlCode(adAppMediaService.getUrlCode(adApp.getId(), adMedia.getId()));
        adAppMedia.setUrl(configUtils.getAppMediaBaseHost() + "/xqh/ad/" + adAppMediaService.getUrlCode(adApp.getId(), adMedia.getId()));
        adAppMedia.setStartCount(getStartCount(dto.getStartCount()));
        adAppMedia.setDiscountRate(getDiscountRate(dto.getDiscountRate()));
        int nowTime = (int) (System.currentTimeMillis()/1000);
        adAppMedia.setCreateTime(nowTime);
        adAppMedia.setUpdateTime(nowTime);

        try
        {
            adAppMediaMapper.insertSelective(adAppMedia);
        } catch (DuplicateKeyException e)
        {
            logger.info("insert appMedia 重复创建 adAppMedia:{}", adAppMedia);
            CommonUtils.sendError(resp, ErrorResponseEunm.DUPLICATE_URLCODE);
            return 0;
        }

        return adAppMedia.getId();

    }

    @Override
    public int update(@RequestBody @Valid @NotNull AdAppMediaUpdateDTO dto,
                      HttpServletResponse resp)
    {
        ErrorResponseEunm errorResponseEunm = validateStartCountAndDiscountRate(dto.getStartCount(), dto.getDiscountRate());
        if (null != errorResponseEunm)
        {
            logger.error("校验不通过 req:{}", JSON.toJSON(dto));
            CommonUtils.sendError(resp, errorResponseEunm);
            return 0;
        }

        AdAppMedia adAppMedia = DozerUtils.map(dto, AdAppMedia.class);

        adAppMedia.setUpdateTime((int) (System.currentTimeMillis()/1000));

        return adAppMediaMapper.updateByPrimaryKeySelective(adAppMedia);

    }

    private Integer getStartCount(Integer startCount) {
        return startCount == null ? Integer.valueOf(configUtils.getDefaultStartCount()) : startCount;
    }

    private Double getDiscountRate(Double discountRate) {
        return discountRate == null ? Double.valueOf(configUtils.getDefaultDiscountRate()) : discountRate;
    }

    private ErrorResponseEunm validateStartCountAndDiscountRate(Integer startCount, Double DiscountRate) {
        if (startCount != null && startCount < 0)
        {
            logger.info("扣量初始值不能为负数 startCount:{}", startCount);
            return ErrorResponseEunm.INVALID_START_COUNT;
        }

        Double d = 1.1;
        List<Double> discountRateList = Arrays.asList(
                Double.valueOf(0),
                Double.valueOf(0.1),
                Double.valueOf(0.2),
                Double.valueOf(0.3),
                Double.valueOf(0.4),
                Double.valueOf(0.5),
                Double.valueOf(0.6),
                Double.valueOf(0.7),
                Double.valueOf(0.8),
                Double.valueOf(0.9),
                Double.valueOf(1));
        if (DiscountRate != null && !discountRateList.contains(DiscountRate))
        {
            logger.info("回调率不合法 discountRate:{}", DiscountRate);
            return ErrorResponseEunm.INVALID_DISCOUNT_RATE;
        }


        return null;
    }


    @Override
    public PageResult<AdAppMediaVO> search(@RequestParam("search") Search search,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Example example = new ExampleBuilder(AdAppMedia.class).search(search).sort(Arrays.asList("id_desc")).build();

        Page<AdAppMedia> appMediaPage = (Page<AdAppMedia>) adAppMediaMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));

        List<AdAppMediaVO> voList = DozerUtils.mapList(appMediaPage.getResult(), AdAppMediaVO.class);
        for (AdAppMediaVO adAppMediaVO : voList)
        {
            adAppMediaVO.setName(adAppMediaVO.getAppName() + "---" + adAppMediaVO.getMediaName());
        }

        return new PageResult<>(appMediaPage.getTotal(), voList);
    }

    @Override
    public String getTestUrl(@RequestParam("id") int id, HttpServletResponse resp)
    {
        AdAppMedia adAppMedia = adAppMediaMapper.selectByPrimaryKey(id);

        if(null == adAppMedia)
        {
            CommonUtils.sendError(resp, ErrorResponseEunm.INVALID_APPID);
            return null;
        }

        String host = testParamConfigUtils.getHost().trim() + adAppMedia.getUrlCode();

        TreeMap<String, String> params = Maps.newTreeMap();

        params.put("key", adAppMedia.getAppKey());
        params.put("ip", testParamConfigUtils.getIp().trim());
        params.put("idfa", testParamConfigUtils.getIdfa().trim());
        params.put("callback", testParamConfigUtils.getCallback().trim());

        return CommonUtils.getFullUrl(host, params);

    }
}
