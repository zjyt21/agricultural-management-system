package com.hlp.agrisys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hlp.agrisys.entity.*;
import com.hlp.agrisys.mapper.MenuMapper;
import com.hlp.agrisys.mapper.RoleMapper;
import com.hlp.agrisys.mapper.RoleMenuMapper;
import com.hlp.agrisys.service.ILoginService;
import com.hlp.agrisys.service.IMenuService;
import com.hlp.agrisys.util.JwtUtil;
import com.hlp.agrisys.util.RedisCache;
import com.hlp.agrisys.vo.UserInfoVo;
import com.hlp.agrisys.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Mr.Han
 * @create 2022-07-14 22:06
 */
@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private IMenuService menuService;

    @Override
    public Result login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应提示
        if(Objects.isNull(authenticate)){//authentication failed
            throw new RuntimeException("failed to login");
        }
        //如果认证通过，使用userid生成jwt，jwt存入Result中进行返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);

        //把完整的用户信息存入redis，userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);

        //把user转换成UserInfoVo
        UserInfoVo userInfoVo = BeanUtil.copyProperties(loginUser.getUser(), UserInfoVo.class);
        userInfoVo.setMenus(loginUser.getMenus());
        // 设置用户的菜单列表
        String roleKey = loginUser.getUser().getRole();
        List<Menu> roleMenus = getRoleMenus(roleKey);
        userInfoVo.setMenus(roleMenus);
        //把token和userinfo封装返回
        UserLoginVo userLoginVo = new UserLoginVo(jwt, userInfoVo);

        return new Result(200, "login successful", userLoginVo);
    }

    @Override
    public Result logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //删除redis中的值
        redisCache.deleteObject("login:" + userid);
        return new Result(200, "Logout succeeded");
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

            if(children.size() != 0 && !roleMenus.contains(menu)) {
                roleMenus.add(menu);
            }
        }
        return roleMenus;
    }
}
