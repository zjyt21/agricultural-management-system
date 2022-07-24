package com.hlp.agrisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hlp.agrisys.entity.*;
import com.hlp.agrisys.mapper.MenuMapper;
import com.hlp.agrisys.mapper.RoleMapper;
import com.hlp.agrisys.mapper.RoleMenuMapper;
import com.hlp.agrisys.mapper.UserMapper;
import com.hlp.agrisys.service.IMenuService;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Han
 * @create 2022-07-14 0:22
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private IMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername, username);
        User user = userMapper.selectOne(lqw);
        //如果没有对应用户，就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("Username does not exist or password is incorrect");
        }
        LoginUser loginUser = new LoginUser();
        //todo 查询对应的权限信息
        String roleKey = user.getRole();
        // 设置用户的菜单列表
        List<Menu> roleMenus = getRoleMenus(roleKey);
        loginUser.setPermissions(roleMenus);

        //把数据封装为UserDetails返回
        return loginUser;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleKey
     * @return
     */
    private List<Menu> getRoleMenus(String roleKey) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleKey, roleKey);
        Long roleId = roleMapper.selectOne(queryWrapper).getId();
        // 当前角色的所有菜单id集合
        List<Long> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
