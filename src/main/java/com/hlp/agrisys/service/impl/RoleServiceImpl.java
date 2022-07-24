package com.hlp.agrisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.Role;
import com.hlp.agrisys.entity.RoleMenu;
import com.hlp.agrisys.mapper.RoleMapper;
import com.hlp.agrisys.mapper.RoleMenuMapper;
import com.hlp.agrisys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * role table 服务实现类
 * </p>
 *
 * @author hlp
 * @since 2022-07-22
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Transactional
    @Override
    public Result setRoleMenu(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(lqw);

        for(Long menuId : menuIds){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
        return Result.success();
    }

    @Override
    public Result getRoleMenu(Long roleId) {
        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId, roleId);
        List<Long> menus = roleMenuMapper.selectList(lqw).stream().map(v -> v.getMenuId()).collect(Collectors.toList());
        return Result.success(menus);
    }
}
