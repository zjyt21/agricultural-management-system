package com.hlp.agrisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlp.agrisys.entity.MarketTrend;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.mapper.MarketTrendMapper;
import com.hlp.agrisys.service.IMarketTrendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <p>
 * service implementation class
 * </p>
 *
 * @author hlp
 * @since 2022-06-29
 */
@Service
public class MarketTrendServiceImpl extends ServiceImpl<MarketTrendMapper, MarketTrend> implements IMarketTrendService {

    @Override
    public Result getMarketPage(int currentPage, int pageSize, String crop, String beginDate, String endDate) {
        LambdaQueryWrapper<MarketTrend> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(crop), MarketTrend::getCrop, crop);
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), MarketTrend::getDate, beginDate, endDate);
        lqw.orderByDesc(MarketTrend::getDate);

        IPage<MarketTrend> page = new Page(currentPage, pageSize);
        page(page, lqw);
        if(page.getPages() < currentPage){
            return getMarketPage((int)page.getPages(), pageSize, crop, beginDate, endDate);
        }
        return new Result(200, page);
    }
}
