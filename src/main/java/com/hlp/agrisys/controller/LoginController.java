package com.hlp.agrisys.controller;

import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.User;
import com.hlp.agrisys.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User authentication related
 *
 * @author Mr.Han
 * @create 2022-07-14 21:54
 */
@RestController
public class LoginController {
    @Autowired
    private ILoginService iLoginService;


    //login
    @PostMapping("/user/login")
    public Result login(@RequestBody User user){
        return iLoginService.login(user);
    }

    //logout
    @GetMapping("/user/logout")
    public Result logout(){
        return iLoginService.logout();
    }

}
