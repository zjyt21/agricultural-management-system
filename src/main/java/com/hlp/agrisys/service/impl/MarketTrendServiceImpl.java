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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private MarketTrendMapper marketTrendMapper;
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

    @Override
    public Result getChart(String beginDate, String endDate) {
        LambdaQueryWrapper<MarketTrend> lqw = new LambdaQueryWrapper<>();
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), MarketTrend::getDate, beginDate, endDate);
        lqw.orderByAsc(MarketTrend::getDate);
        String[] crops = new String[]{"paddy", "potato", "soybean", "peanut", "wheat", "barley"};
        List<List<MarketTrend>> marketTrends = new ArrayList<>();
        for(String crop : crops){
            List<MarketTrend> chartByCrop = getChartByCrop(beginDate, endDate, crop);
            marketTrends.add(chartByCrop);
        }
        return new Result(200, marketTrends);
    }

    public List<MarketTrend> getChartByCrop(String beginDate, String endDate, String crop){
        LambdaQueryWrapper<MarketTrend> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.isNotBlank(crop), MarketTrend::getCrop, crop);
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), MarketTrend::getDate, beginDate, endDate);
        lqw.orderByAsc(MarketTrend::getDate);
        return marketTrendMapper.selectList(lqw);
    }
}
