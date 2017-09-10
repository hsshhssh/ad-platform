package com.xqh.ad.controller.impl;

import com.github.pagehelper.Page;
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

        adAppMedia.setMediaCode(adMedia.getCode());
        adAppMedia.setUrlCode(adAppMediaService.getUrlCode(adApp.getId(), adMedia.getId()));
        adAppMedia.setUrl("http://ad.uerbx.com/xqh/ad/" + adAppMediaService.getUrlCode(adApp.getId(), adMedia.getId()));
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
    public int update(@RequestBody @Valid @NotNull AdAppMediaUpdateDTO dto)
    {
        AdAppMedia adAppMedia = DozerUtils.map(dto, AdAppMedia.class);

        adAppMedia.setUpdateTime((int) (System.currentTimeMillis()/1000));

        return adAppMediaMapper.updateByPrimaryKeySelective(adAppMedia);

    }

    @Override
    public PageResult<AdAppMediaVO> search(@RequestParam("search") Search search,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Example example = new ExampleBuilder(AdAppMedia.class).search(search).sort(Arrays.asList("id_desc")).build();

        Page<AdAppMedia> appMediaPage = (Page<AdAppMedia>) adAppMediaMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));

        return new PageResult<>(appMediaPage.getTotal(), DozerUtils.mapList(appMediaPage.getResult(), AdAppMediaVO.class));
    }
}
