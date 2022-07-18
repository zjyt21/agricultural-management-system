package com.hlp.agrisys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Mr.Han
 * @create 2022-07-16 16:50
 */
@Data
@Accessors(chain = true)
public class UserInfoVo {
    /**
     * id
     */
    private Long id;

    /**
     * username
     */
    private String username;

    /**
     * nickname
     */
    private String nickname;

    /**
     * phonenumber
     */
    private String phonenumber;

    /**
     * sex
     */
    private String sex;

    /**
     * avatar
     */
    private String avatar;

    /**
     * email
     */
    private String email;
}
