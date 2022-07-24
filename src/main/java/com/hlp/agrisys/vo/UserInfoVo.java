package com.hlp.agrisys.vo;

import com.hlp.agrisys.entity.Menu;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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

    private String role;

    private List<Menu> permissions;
}
