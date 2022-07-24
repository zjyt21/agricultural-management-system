package com.hlp.agrisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hlp.agrisys.entity.Menu;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.mapper.MenuMapper;
import com.hlp.agrisys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * menu table 服务实现类
 * </p>
 *
 * @author hlp
 * @since 2022-07-22
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> findMenus(String name) {
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(name), Menu::getName, name);
        List<Menu> list = list(lqw);
        //pid is null, root menu
        List<Menu> parents = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());

        //find root menu's subMenu
        for (Menu menu : parents) {
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parents;
    }
}
