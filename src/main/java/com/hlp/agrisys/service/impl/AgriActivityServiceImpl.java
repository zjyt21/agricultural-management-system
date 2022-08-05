package com.hlp.agrisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlp.agrisys.entity.AgriActivity;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.mapper.AgriActivityMapper;
import com.hlp.agrisys.service.IAgriActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private AgriActivityMapper agriActivityMapper;

    @Override
    public Result getActivityPage(int currentPage, int pageSize, String type, String beginDate, String endDate, String crop) {
        LambdaQueryWrapper<AgriActivity> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(crop), AgriActivity::getCrop, crop);
        lqw.like(StringUtils.isNotBlank(type), AgriActivity::getType, type);
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), AgriActivity::getDate, beginDate, endDate);
        lqw.orderByDesc(AgriActivity::getDate);

        IPage<AgriActivity> page = new Page(currentPage, pageSize);
        page(page, lqw);
        if(page.getPages() < currentPage){
            return getActivityPage((int)page.getPages(), pageSize, type, beginDate, endDate, crop);
        }
        return Result.success(page);
    }

    @Override
    public Result getChart(String crop, String beginDate, String endDate) {
        LambdaQueryWrapper<AgriActivity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AgriActivity::getCrop, crop);
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), AgriActivity::getDate, beginDate, endDate);
        lqw.orderByAsc(AgriActivity::getDate);
        String[] activities = new String[]{"sowing", "watering", "nitrogen", "phosphate", "harvest"};
        List<List<AgriActivity>> agriActivities = new ArrayList<>();
        for(String activity : activities){
            List<AgriActivity> chartByActivity = getChartByActivity(crop, beginDate, endDate, activity);
            agriActivities.add(chartByActivity);
        }
        return Result.success(agriActivities);
    }

    @Override
    public Result getTask() {
        int[] task = new int[5];
        LambdaQueryWrapper<AgriActivity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AgriActivity::getType, "harvest");
        lqw.orderByDesc(AgriActivity::getDate).last("limit 1");
        AgriActivity lastHarvest = agriActivityMapper.selectOne(lqw);

        LambdaQueryWrapper<AgriActivity> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(AgriActivity::getType, "sowing");
        lqw2.ge(AgriActivity::getDate, lastHarvest.getDate());
        AgriActivity lastSowing = agriActivityMapper.selectOne(lqw2);
        if(lastSowing == null) {
            LambdaQueryWrapper<AgriActivity> lqw3 = new LambdaQueryWrapper<>();
            lqw3.eq(AgriActivity::getType, "sowing");
            List<AgriActivity> sowings = agriActivityMapper.selectList(lqw3);
            int sum = 0;
            for(AgriActivity a : sowings){
                sum += a.getQuantity();
            }
            task[0] = sum / sowings.size();
            return Result.success(task);
        }

        String crop = lastSowing.getCrop();
        LambdaQueryWrapper<AgriActivity> lqw3 = new LambdaQueryWrapper<>();
        lqw3.ge(AgriActivity::getDate, lastHarvest.getDate());
        List<AgriActivity> curSeason = agriActivityMapper.selectList(lqw3);
        int size = curSeason.size();
        for(AgriActivity a : curSeason){
            switch (a.getType()){
                case "watering":
                    task[1] += a.getQuantity() / size / 2;
                    break;
                case "nitrogen":
                    task[2] += a.getQuantity() / size / 2;
                    break;
                case "phosphate":
                    task[3] += a.getQuantity() / size / 2;
                    break;
            }
        }
        task[1] += 350;
        task[2] += 23;
        task[3] += 30;
        return Result.success(task);
    }

    private List<AgriActivity> getChartByActivity(String crop, String beginDate, String endDate, String activity) {
        LambdaQueryWrapper<AgriActivity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.isNotBlank(activity), AgriActivity::getType, activity);
        lqw.eq(StringUtils.isNotBlank(activity), AgriActivity::getCrop, crop);
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), AgriActivity::getDate, beginDate, endDate);
        lqw.orderByAsc(AgriActivity::getDate);
        return agriActivityMapper.selectList(lqw);

    }
}
