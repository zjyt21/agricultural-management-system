package com.hlp.agrisys.service;

import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.Soil;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hlp
 * @since 2022-08-09
 */
public interface ISoilService extends IService<Soil> {

    Result getSoilConditionPage(int currentPage, int pageSize, String beginDate, String endDate);

    Result getChart(String beginDate, String endDate);
}
