package com.hlp.agrisys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * user table
 * </p>
 *
 * @author hlp
 * @since 2022-07-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User Object", description="user table")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "username")
    private String username;

    @ApiModelProperty(value = "nickname")
    private String nickname;

    @ApiModelProperty(value = "password")
    private String password;

    @ApiModelProperty(value = "Account status (0 normal, 1 disabled)")
    private String status;

    @ApiModelProperty(value = "email")
    private String email;

    @ApiModelProperty(value = "phone number")
    private String phonenumber;

    @ApiModelProperty(value = "User gender (0 male, 1 female, 2 unknown)")
    private String sex;

    @ApiModelProperty(value = "avatar")
    private String avatar;

    @ApiModelProperty(value = "User Role")
    private String role;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "Delete flag (0 means not deleted, 1 means deleted)")
    private Integer deleted;

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
