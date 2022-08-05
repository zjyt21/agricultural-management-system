package com.hlp.agrisys.controller;

import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.User;
import com.hlp.agrisys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/userInfo")
    public Result updateUserInfo(@RequestBody User user){
        return iUserService.updateUserInfo(user);
    }

    @GetMapping("page")
    public Result pageSelect(@RequestParam int currentPage,
                             @RequestParam int pageSize,
                             @RequestParam(defaultValue = "") String username,
                             @RequestParam(defaultValue = "") String nickname,
                             @RequestParam(defaultValue = "") String email){
        return iUserService.getUserPage(currentPage, pageSize, username, nickname, email);
    }

    //Save or update user information
    @PostMapping
    public Result saveOrUpdateMarketTrend(@RequestBody User user){
        return Result.success(iUserService.saveOrUpdate(user));
    }

    //delete user by id
    @DeleteMapping("{id}")
    public Result deleteMarketTrendById(@PathVariable String id){
        return Result.success(iUserService.removeById(id));
    }

    //delete market information by several id
    @PostMapping("/batchDel")
    public Result batchDeleteMarketTrend(@RequestBody List<String> ids){
        return Result.success(iUserService.removeByIds(ids));
    }

}
