package com.hlp.agrisys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.User;

/**
 * <p>
 * user table 服务类
 * </p>
 *
 * @author hlp
 * @since 2022-07-14
 */
public interface IUserService extends IService<User> {

    Result register(User user);

    Result getUserInfoByUsername(String username);

    Result userInfo();

    Result updateUserInfo(User user);

    Result getUserPage(int currentPage, int pageSize, String username, String nickname, String email);
}
