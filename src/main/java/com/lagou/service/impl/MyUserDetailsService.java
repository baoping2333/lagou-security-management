package com.lagou.service.impl;

import com.lagou.domain.User;
import com.lagou.service.UserService;
import jdk.jfr.internal.tool.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 基于数据库中完成认证
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);// 用户名没有找到
        }
        // 先声明一个权限集合, 因为构造方法里面不能传入null
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        // 需要返回一个SpringSecurity的UserDetails对象
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
//                "{noop}" + user.getPassword(),// {noop}表示不加密认证。
                "{bcrypt}" + user.getPassword(),// {noop}表示不加密认证。
                true, // 用户是否启用true 代表启用
                true,// 用户是否过期 true 代表未过期
                true,// 用户凭据是否过期 true 代表未过期
                true,// 用户是否锁定 true 代表未锁定
                authorities);
        return userDetails;

    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String admin = bCryptPasswordEncoder.encode("admin");
        System.out.println(admin);
    }
}
