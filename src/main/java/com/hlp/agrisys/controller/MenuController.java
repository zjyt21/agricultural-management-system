package com.hlp.agrisys.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlp.agrisys.entity.Constants;
import com.hlp.agrisys.entity.Dict;
import com.hlp.agrisys.entity.Menu;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.service.IDictService;
import com.hlp.agrisys.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * menu table 前端控制器
 * </p>
 *
 * @author hlp
 * @since 2022-07-22
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private IMenuService menuService;

    @Autowired
    private IDictService dictService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        menuService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        menuService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.success(menuService.findMenus(name));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer currentPage,
                           @RequestParam Integer pageSize) {
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(name), Menu::getName, name);
        lqw.orderByDesc(Menu::getId);
        return Result.success(menuService.page(new Page<>(currentPage, pageSize), lqw));
    }

    @GetMapping("/icons")
    public Result getIcons(){
        LambdaQueryWrapper<Dict> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dict::getType, Constants.DICT_TYPE_ICON);
        return Result.success(dictService.list(lqw));
    }
}
