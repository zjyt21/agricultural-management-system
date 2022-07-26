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
        return new LoginUser(user);
    }
}
