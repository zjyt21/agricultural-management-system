package com.hlp.agrisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.Soil;
import com.hlp.agrisys.mapper.SoilMapper;
import com.hlp.agrisys.service.ISoilService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hlp
 * @since 2022-08-09
 */
@Service
public class SoilServiceImpl extends ServiceImpl<SoilMapper, Soil> implements ISoilService {

    @Override
    public Result getSoilConditionPage(int currentPage, int pageSize, String beginDate, String endDate) {
        LambdaQueryWrapper<Soil> lqw = new LambdaQueryWrapper<>();
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), Soil::getDate, beginDate, endDate);
        lqw.orderByDesc(Soil::getDate);

        IPage<Soil> page = new Page(currentPage, pageSize);
        page(page, lqw);
        if(page.getPages() < currentPage){
            return getSoilConditionPage((int)page.getPages(), pageSize, beginDate, endDate);
        }
        return Result.success(page);
    }
}
