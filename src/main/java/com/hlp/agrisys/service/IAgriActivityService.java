package com.hlp.agrisys.service;

import com.hlp.agrisys.entity.AgriActivity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hlp.agrisys.entity.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hlp
 * @since 2022-07-26
 */
public interface IAgriActivityService extends IService<AgriActivity> {

    Result getActivityPage(int currentPage, int pageSize, String type, String beginDate, String endDate, String crop);

    Result getChart(String crop, String beginDate, String endDate);
}
