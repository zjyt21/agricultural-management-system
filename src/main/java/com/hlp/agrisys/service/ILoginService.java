package com.hlp.agrisys.service;

import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.User;

/**
 * @author Mr.Han
 * @create 2022-07-14 22:06
 */
public interface ILoginService {
    Result login(User user);

    Result logout();

}
