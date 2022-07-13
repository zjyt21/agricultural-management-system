package com.hlp.agrisys.controller;


import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlp.agrisys.entity.MarketTrend;
import com.hlp.agrisys.service.IMarketTrendService;
import freemarker.template.utility.DateUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * <p>
 *  web controller
 * </p>
 *
 * @author hlp
 * @since 2022-06-29
 */
@RestController
@RequestMapping("/market-trend")
public class MarketTrendController {
    @Autowired
    private IMarketTrendService iMarketTrendService;

    //Get all market information
    @GetMapping
    public List<MarketTrend> listMarketTrends(){
        return iMarketTrendService.list();
    }

    //Save or update market information
    @PostMapping
    public boolean saveOrUpdateMarketTrend(@RequestBody MarketTrend marketTrend){
        return iMarketTrendService.saveOrUpdate(marketTrend);
    }

    //delete market information by id
    @DeleteMapping("{id}")
    public boolean deleteMarketTrendById(@PathVariable String id){
        return iMarketTrendService.removeById(id);
    }

    //delete market information by id
    @PostMapping("/batchDel")
    public boolean batchDeleteMarketTrend(@RequestBody List<String> ids){
        return iMarketTrendService.removeByIds(ids);
    }

    //pagination query
    @GetMapping("/page")
    public IPage<MarketTrend> pageSelect(@RequestParam int currentPage,
                                         @RequestParam int pageSize,
                                         @RequestParam(defaultValue = "") String crop,
                                         @RequestParam(defaultValue = "") String beginDate,
                                         @RequestParam(defaultValue = "") String endDate){
        LambdaQueryWrapper<MarketTrend> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(crop), MarketTrend::getCrop, crop);
        lqw.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), MarketTrend::getDate, beginDate, endDate);
        lqw.orderByDesc(MarketTrend::getDate);
        IPage<MarketTrend> page = new Page(currentPage, pageSize);
        iMarketTrendService.page(page, lqw);
        return page;
    }
}
