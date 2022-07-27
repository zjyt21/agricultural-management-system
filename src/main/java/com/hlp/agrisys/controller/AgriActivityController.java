package com.hlp.agrisys.controller;


import com.hlp.agrisys.entity.AgriActivity;
import com.hlp.agrisys.entity.MarketTrend;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.service.IAgriActivityService;
import com.hlp.agrisys.service.IUserService;
import com.hlp.agrisys.service.impl.UserServiceImpl;
import com.hlp.agrisys.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hlp
 * @since 2022-07-26
 */
@RestController
@RequestMapping("/agri-activity")
public class AgriActivityController {
    @Autowired
    private IAgriActivityService agriActivityService;
    @Autowired
    private IUserService userService;

    @GetMapping
    public Result getAllActivities() {
        return Result.success(agriActivityService.list());
    }

    //pagination query
    @GetMapping("/page")
    public Result pageSelect(@RequestParam int currentPage,
                             @RequestParam int pageSize,
                             @RequestParam(defaultValue = "") String type,
                             @RequestParam(defaultValue = "") String beginDate,
                             @RequestParam(defaultValue = "") String endDate,
                             @RequestParam(defaultValue = "") String crop) {
        return agriActivityService.getActivityPage(currentPage, pageSize, type, beginDate, endDate, crop);
    }


    //Save or update
    @PostMapping
    public Result saveOrUpdateActivity(@RequestBody AgriActivity agriActivity) {
        UserInfoVo vo = (UserInfoVo) userService.userInfo().getData();
        agriActivity.setOperator(vo.getId());
        return Result.success(agriActivityService.saveOrUpdate(agriActivity));
    }

    //delete by id
    @DeleteMapping("{id}")
    public Result deleteActivityById(@PathVariable Long id) {
        return Result.success(agriActivityService.removeById(id));
    }

    //delete by ids
    @PostMapping("/batchDel")
    public Result batchDeleteActivities(@RequestBody List<Long> ids) {
        return Result.success(agriActivityService.removeByIds(ids));
    }

    //echarts data
    @GetMapping("/chart")
    public Result getChart(@RequestParam(defaultValue = "paddy") String crop,
                           @RequestParam(defaultValue = "") String beginDate,
                           @RequestParam(defaultValue = "") String endDate)
    {
        return agriActivityService.getChart(crop, beginDate, endDate);
    }
}