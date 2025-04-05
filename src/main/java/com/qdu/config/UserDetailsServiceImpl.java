package com.qdu.config;

import com.qdu.entity.Users;
import com.qdu.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersServiceImpl userService;

    // 根据用户名查询用户信息,用于登录时使用
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userService.lambdaQuery()
                .eq(Users::getUserName, username)
                .one();
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 返回一个Spring Security的默认 UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getEncryptedPassword(),
                Collections.emptyList()
        );
    }
}