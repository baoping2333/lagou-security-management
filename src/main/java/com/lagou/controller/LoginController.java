package com.lagou.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理登录业务
 */
@Controller
public class LoginController {
    /**
     * 跳转登录页面
     *
     * @return
     */
    @RequestMapping("/toLoginPage")
    public String toLoginPage() {
        return "login";
    }


}
