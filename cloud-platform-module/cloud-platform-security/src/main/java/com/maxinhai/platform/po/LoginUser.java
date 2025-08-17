package com.maxinhai.platform.po;

import com.maxinhai.platform.vo.UserVO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class LoginUser implements UserDetails {

    private String id;

    private String username; // 登录用户名

    private String password; // 加密后的密码

    private String nickname; // 昵称（可选）

    private boolean enabled = true; // 账户是否启用

    private boolean accountNonExpired = true;  // 账户是否未过期

    private boolean accountNonLocked = true;  // 账户是否未锁定

    private boolean credentialsNonExpired = true;  // 凭证是否未过期

    // 用户角色，多个角色用逗号分隔
    private String roles;

    // 实现UserDetails接口方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null && !roles.isEmpty()) {
            String[] roleArray = roles.split(",");
            for (String role : roleArray) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        return authorities;
    }

    /**
     * VO转DTO
     * @param user
     * @return
     */
    public static LoginUser convert(UserVO user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setUsername(user.getAccount());
        loginUser.setPassword(user.getPassword());
        loginUser.setNickname(user.getUsername());
        loginUser.setEnabled(false);
        return loginUser;
    }

}
