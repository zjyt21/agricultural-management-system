package com.hlp.agrisys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlp.agrisys.entity.MarketTrend;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.User;
import com.hlp.agrisys.mapper.UserMapper;
import com.hlp.agrisys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlp.agrisys.util.RedisCache;
import com.hlp.agrisys.util.SecurityUtils;
import com.hlp.agrisys.vo.UserInfoVo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.hlp.agrisys.util.JwtUtil.parseJWT;

/**
 * <p>
 * user table 服务实现类
 * </p>
 *
 * @author hlp
 * @since 2022-07-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG = Log.get();
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;

    //register
    @Override
    public Result register(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String username = user.getUsername();
        String password = user.getPassword();
        String nickname = user.getNickname();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(nickname)){
            return new Result(400, "Wrong parameter");
        }
        User userInfo = getUserInfo(user);
        if(userInfo == null){
            userInfo = new User();
            BeanUtils.copyProperties(user, userInfo);
            userInfo.setPassword(encoder.encode(password));
            save(userInfo);
        }else {
            return new Result(400, "User already exists");
        }
        return new Result(200, userInfo);
    }

    @Override
    public Result getUserInfoByUsername(String username) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotBlank(username), User::getUsername, username);
        UserInfoVo userInfoVo = new UserInfoVo();
        User user = getOne(lqw);
        BeanUtils.copyProperties(user, userInfoVo);
        return new Result(200, userInfoVo);
    }

    //get user information by current Token
    @Override
    public Result userInfo() {
        //get current user id
        Long userId = SecurityUtils.getUserId();
        //get info by id
        User user = getById(userId);
        //packaged into UserInfoVo
        UserInfoVo vo = BeanUtil.copyProperties(user, UserInfoVo.class);
        System.out.println(vo.getPhonenumber());
        return new Result(200, vo);
    }

    //update user information
    @Override
    public Result updateUserInfo(User user) {
        updateById(user);
        return new Result(200, "操作成功");
    }

    // get user information by entity
    private User getUserInfo(User user){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername, user.getUsername());
        User one = null;
        try {
            one = getOne(lqw);
        }catch (Exception e){
            LOG.error(e);
            throw new RuntimeException("System error");
        }
        return one;
    }

    // get current user
    public User getCurrentUser(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");

            if (StrUtil.isNotBlank(token)) {
                String userId = parseJWT(token).getSubject();
                String redisKey = "login:" + userId;
                return redisCache.getCacheObject(redisKey);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    //get user's list
    @Override
    public Result getUserPage(int currentPage, int pageSize, String username, String nickname, String email) {
        username = username.trim();
        nickname = nickname.trim();
        email = email.trim();
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(username), User::getUsername, username);
        lqw.like(StringUtils.isNotBlank(nickname), User::getNickname, nickname);
        lqw.like(StringUtils.isNotBlank(email), User::getEmail, email);

        IPage<User> page = new Page(currentPage, pageSize);
        page(page, lqw);
        if(page.getPages() < currentPage){
            return getUserPage((int)page.getPages(), pageSize, username, nickname, email);
        }
        return new Result(200, page);
    }
}
