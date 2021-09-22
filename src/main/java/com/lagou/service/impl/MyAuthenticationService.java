package com.lagou.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录成功或失败处理类
 */
@Service
public class MyAuthenticationService implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {
    //    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication
//                                                authentication) throws IOException, ServletException {
//    }
    @Autowired
    ObjectMapper objectMapper;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication) throws
            IOException, ServletException {
        System.out.println("退出成功后续处理....");
        redirectStrategy.sendRedirect(request, response, "/toLoginPage");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws
            IOException, ServletException {
        System.out.println("登录成功后续处理...."); //重定向到index页
//        redirectStrategy.sendRedirect(request, response, "/");

        Map result = new HashMap();
        result.put("code", HttpStatus.OK.value());// 设置响应码
        result.put("message", "登录成功");// 设置响应信息
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws
            IOException, ServletException {
        System.out.println("登录失败后续处理...."); //重定向到login页
//        redirectStrategy.sendRedirect(request, response, "/toLoginPage");
        Map result = new HashMap();
        result.put("code", HttpStatus.UNAUTHORIZED.value());// 设置响应码
        result.put("message", exception.getMessage());// 设置错误信息
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
