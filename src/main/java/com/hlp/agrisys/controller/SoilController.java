package com.hlp.agrisys.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.Soil;
import com.hlp.agrisys.service.ISoilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hlp
 * @since 2022-08-09
 */
@RestController
@RequestMapping("/soil")
public class SoilController {
    @Autowired
    private ISoilService soilService;

    @PostMapping
    public Result saveOrUpdateSoilCondition(@RequestBody Soil soil){
        soil.setScore(getSoilScore(soil));
        boolean update = soilService.saveOrUpdate(soil);
        return update ? Result.success() : Result.error();
    }

    //delete soil condition by id
    @DeleteMapping("{id}")
    public Result deleteSoilConditionById(@PathVariable String id){
        return Result.success(soilService.removeById(id));
    }

    //delete soil condition records by ids
    @PostMapping("/batchDel")
    public Result batchDeleteSoilCondition(@RequestBody List<String> ids){
        return Result.success(soilService.removeByIds(ids));
    }

    //pagination query
    @GetMapping("/page")
    public Result pageSelect(@RequestParam int currentPage,
                             @RequestParam int pageSize,
                             @RequestParam(defaultValue = "") String beginDate,
                             @RequestParam(defaultValue = "") String endDate)
    {
        return soilService.getSoilConditionPage(currentPage, pageSize, beginDate, endDate);
    }

    public Double getSoilScore(Soil soil){
        Double compaction = soil.getCompaction(); // 5 - 15
        Double soilTemperature = soil.getSoilTemperature(); // 27 - 32
        Double ph = soil.getPh(); // 5.5 - 6.5
        Double water = soil.getWater(); // 15.5 - 18.5
        Double nitrogen = soil.getNitrogen(); // 0.5 - 0.6
        Double phosphorus = soil.getPhosphorus(); // 0.2 - 0.25

        Double score = 0.0;
        if(compaction <= 10) score += 100 / 6;
        else if(compaction < 20) score += 75 / 6;
        else if(compaction < 30) score += 50 / 6;
        else score += 25 / 6;

        if(soilTemperature >= 27 && soilTemperature <= 32) score += 100 / 6;
        else if(soilTemperature < 37 && soilTemperature > 22) score += 75 / 6;
        else if(soilTemperature < 42 && soilTemperature > 17) score += 50 / 6;
        else score += 25 / 6;

        if(ph >= 5.5 && ph <= 6.5) score += 100 / 6;
        else if(ph < 7 && ph > 5) score += 75 / 6;
        else if(ph < 7.5 && ph > 4.5) score += 50 / 6;
        else score += 25 / 6;

        if(water >= 15.5 && water <= 18.5) score += 100 / 6;
        else if(water < 22 && water > 12) score += 75 / 6;
        else if(water < 25 && water > 8) score += 50 / 6;
        else score += 25 / 6;

        if(nitrogen >= 0.5 && nitrogen <= 0.6) score += 100 / 6;
        else if(nitrogen < 0.8 && nitrogen > 0.3) score += 75 / 6;
        else if(nitrogen < 1 && nitrogen > 0.03) score += 50 / 6;
        else score += 25 / 6;

        if(phosphorus >= 0.2 && phosphorus <= 0.3) score += 100 / 6;
        else if(phosphorus < 0.4 && phosphorus > 0.1) score += 75 / 6;
        else if(phosphorus < 0.5 && phosphorus > 0.05) score += 50 / 6;
        else score += 25 / 6;

        return score;
    }
}
