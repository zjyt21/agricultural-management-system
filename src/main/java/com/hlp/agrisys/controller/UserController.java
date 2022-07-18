package com.hlp.agrisys.controller;

import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.User;
import com.hlp.agrisys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * user table 前端控制器
 * </p>
 *
 * @author hlp
 * @since 2022-07-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    //register
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        return iUserService.register(user);
    }

    //get user by token
    @GetMapping("/userInfo")
    public Result getUserInfoByToken(){
        return iUserService.userInfo();
    }

    //save or update user
    @PostMapping
    public Result saveOrUpdateUser(@RequestBody User user){
        return new Result(200, iUserService.saveOrUpdate(user));
    }

    @PutMapping("/userInfo")
    public Result updateUserInfo(@RequestBody User user){
        return iUserService.updateUserInfo(user);
    }

}
