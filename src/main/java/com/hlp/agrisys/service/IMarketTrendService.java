package com.hlp.agrisys.service;

import com.hlp.agrisys.entity.MarketTrend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hlp.agrisys.entity.Result;

/**
 * <p>
 *  service class
 * </p>
 *
 * @author hlp
 * @since 2022-06-29
 */
public interface IMarketTrendService extends IService<MarketTrend> {

    Result getMarketPage(int currentPage, int pageSize, String crop, String beginDate, String endDate);
}
