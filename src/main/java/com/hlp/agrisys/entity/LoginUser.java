package com.hlp.agrisys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author Mr.Han
 * @create 2022-07-14 0:25
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private User user;
    private List<Menu> menus;

    public LoginUser(User user) {
        this.user = user;
    }

    public LoginUser(User user, List<Menu> menus) {
        this.user = user;
        this.menus = menus;
    }

    @JSONField(serialize = false)//这样该成员变量就不会被序列化，因为redis出于安全考虑，如果需要序列化这个成员变量，会报异常
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//获取权限信息
//        if(authorities != null){
//            return authorities;
//        }
////        把permission中String类型的权限信息封装成SimpleGrantedAuthority对象
//        authorities = new ArrayList<>();
//        for (Menu permission : permissions){
//            String permissionName = permission.getName();
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionName);
//            authorities.add(authority);
//        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus().equals("0") ? true : false;
    }
}
