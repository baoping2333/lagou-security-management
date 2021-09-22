package com.lagou.filter;

import com.lagou.controller.ValidateCodeController;
import com.lagou.exception.ValidateCodeException;
import com.lagou.service.impl.MyAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码验证filter 需要继承OncePerRequestFilter确保在一次请求只通过一次filter，而不
 * 需要重复执行
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    @Autowired
    MyAuthenticationService myAuthenticationService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, FilterChain filterChain) throws
            ServletException, IOException {
        {
            // 判断是否是登录请求,只有登录请求才去校验验证码
            if (httpServletRequest.getRequestURI().equals("/login")
                    && httpServletRequest.getMethod().equalsIgnoreCase("post"))
                try {
                    validate(httpServletRequest);
                } catch (ValidateCodeException e) {
                    myAuthenticationService.onAuthenticationFailure(httpServletRequest,
                            httpServletResponse, e);
                    return;
                }
        }
        //如果不是登录请求，直接调用后面的过滤器链
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
         //获取ip
        String remoteAddr = request.getRemoteAddr();
        //拼接redis的key
        String redisKey = ValidateCodeController.REDIS_KEY_IMAGE_CODE + "-"
                + remoteAddr; //从redis中获取imageCode
        String redisImageCode =
                stringRedisTemplate.boundValueOps(redisKey).get();
        String imageCode = request.getParameter("imageCode");
        if (!StringUtils.hasText(imageCode)) {
            throw new ValidateCodeException("验证码的值不能为空!");
        }
        if (redisImageCode == null) {
            throw new ValidateCodeException("验证码已过期!");
        }
        if (!redisImageCode.equals(imageCode)) {
            throw new ValidateCodeException("验证码不正确!");
        }
// 从redis中删除imageCode
        stringRedisTemplate.delete(redisKey);
    }
}

