package com.hlp.agrisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlp.agrisys.entity.AgriActivity;
import com.hlp.agrisys.entity.MarketTrend;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.mapper.AgriActivityMapper;
import com.hlp.agrisys.service.IAgriActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hlp
 * @since 2022-07-26
 */
@Service
public class AgriActivityServiceImpl extends ServiceImpl<AgriActivityMapper, AgriActivity> implements IAgriActivityService {

    @Override
    public Result getActivityPage(int currentPage, int pageSize, String type, String beginDate, String endDate, String crop) {
        LambdaQueryWrapper<AgriActivity> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(crop), AgriActivity::getCrop, crop);
        lqw.like(StringUtils.isNotBlank(type), AgriActivity::getType, type);
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), AgriActivity::getDate, beginDate, endDate);
//        lqw.orderByDesc(AgriActivity::getDate);

        IPage<AgriActivity> page = new Page(currentPage, pageSize);
        page(page, lqw);
        if(page.getPages() < currentPage){
            return getActivityPage((int)page.getPages(), pageSize, type, beginDate, endDate, crop);
        }
        return Result.success(page);
    }

}
