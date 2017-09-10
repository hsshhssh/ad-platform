package com.xqh.ad.controller.impl;

import com.github.pagehelper.Page;
import com.xqh.ad.controller.api.IAdMediaController;
import com.xqh.ad.entity.dto.AdMediaCreateDTO;
import com.xqh.ad.entity.dto.AdMediaUpdateDTO;
import com.xqh.ad.entity.vo.AdMediaVO;
import com.xqh.ad.service.AdMediaService;
import com.xqh.ad.tkmapper.entity.AdMedia;
import com.xqh.ad.tkmapper.mapper.AdMediaMapper;
import com.xqh.ad.utils.common.DozerUtils;
import com.xqh.ad.utils.common.ExampleBuilder;
import com.xqh.ad.utils.common.PageResult;
import com.xqh.ad.utils.common.Search;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdMediaController implements IAdMediaController
{
    private static Logger logger = LoggerFactory.getLogger(AdMediaController.class);

    @Autowired
    private AdMediaMapper adMediaMapper;

    @Autowired
    private AdMediaService adMediaService;

    //TODO @Transactional 这个地方加了注解好奇怪 接口全不能用
    @Override
    public int insert(@RequestBody @Valid @NotNull AdMediaCreateDTO dto, HttpServletResponse resp)
    {
        AdMedia adMedia = DozerUtils.map(dto, AdMedia.class);

        //adMediaMapper.insertSelective(adMedia);
        //
        //adMedia.setCode(String.format("%04d", adMedia.getId()));
        //
        //adMediaMapper.updateByPrimaryKeySelective(adMedia);
        //
        //return adMedia.getId();
        return adMediaService.insert(adMedia);
    }

    @Override
    public int update(@RequestBody @Valid @NotNull AdMediaUpdateDTO dto)
    {
        AdMedia adMedia = DozerUtils.map(dto, AdMedia.class);

        adMediaMapper.updateByPrimaryKeySelective(adMedia);

        return 1;
    }

    @Override
    public PageResult<AdMediaVO> search(@RequestParam("search") Search search,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Example example = new ExampleBuilder(AdMedia.class).search(search).sort(Arrays.asList("id_desc")).build();

        Page<AdMedia> mediaPage = (Page<AdMedia>) adMediaMapper.selectByExampleAndRowBounds(example, new RowBounds(page, size));

        return new PageResult<>(mediaPage.getTotal(), DozerUtils.mapList(mediaPage.getResult(), AdMediaVO.class));
    }
}
