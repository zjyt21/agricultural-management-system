package com.hlp.agrisys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hlp.agrisys.entity.LoginUser;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.User;
import com.hlp.agrisys.service.ILoginService;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

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
        userInfoVo.setPermissions(loginUser.getPermissions());
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

}
