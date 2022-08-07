package com.hlp.agrisys.vo;

import lombok.Data;

/**
 * @author Mr.Han
 * @create 2022-08-07 18:31
 */
@Data
public class UserPasswordVo {
    private String username;
    private String password;
    private String newPassword;
}
