package com.hlp.agrisys.controller;


import com.hlp.agrisys.entity.AgriActivity;
import com.hlp.agrisys.entity.MarketTrend;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.service.IMarketTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result listMarketTrends(){
        return new Result(200, iMarketTrendService.list());
    }

    //Save or update market information
    @PostMapping
    public Result saveOrUpdateMarketTrend(@RequestBody MarketTrend marketTrend){
        return new Result(200, iMarketTrendService.saveOrUpdate(marketTrend));
    }

    //delete market information by id
    @DeleteMapping("{id}")
    public Result deleteMarketTrendById(@PathVariable String id){
        return new Result(200, iMarketTrendService.removeById(id));
    }

    //delete market information by ids
    @PostMapping("/batchDel")
    public Result batchDeleteMarketTrend(@RequestBody List<String> ids){
        return new Result(200, iMarketTrendService.removeByIds(ids));
    }

    //pagination query
    @GetMapping("/page")
    public Result pageSelect(@RequestParam int currentPage,
                             @RequestParam int pageSize,
                             @RequestParam(defaultValue = "") String crop,
                             @RequestParam(defaultValue = "") String beginDate,
                             @RequestParam(defaultValue = "") String endDate)
    {
        return iMarketTrendService.getMarketPage(currentPage, pageSize, crop, beginDate, endDate);
    }

    @PostMapping("/batchSave")
    public Result batchSaveOrUpdateMarket(@RequestBody List<MarketTrend> marketTrends) {
        for(MarketTrend m : marketTrends){
            iMarketTrendService.save(m);
        }
        return Result.success();
    }

    //echarts data
    @GetMapping("/chart")
    public Result getChart(@RequestParam(defaultValue = "") String beginDate,
                           @RequestParam(defaultValue = "") String endDate)
    {
        return iMarketTrendService.getChart(beginDate, endDate);
    }
}
