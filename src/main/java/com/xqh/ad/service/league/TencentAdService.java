package com.xqh.ad.service.league;

import com.xqh.ad.tkmapper.entity.AdApp;
import com.xqh.ad.tkmapper.entity.AdAppMedia;
import com.xqh.ad.tkmapper.entity.AdClick;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hssh on 2017/8/12.
 */
@Service
public class TencentAdService extends LeagueAbstractService
{

    @Override
    public void redirectUrl(HttpServletRequest req, HttpServletResponse resp, AdApp adApp, AdAppMedia adAppMedia, AdClick adClick) {

    }
}
