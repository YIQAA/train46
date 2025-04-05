package com.qdu.common;

import com.qdu.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    // 拦截器，验证token，
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            // 解析token，获取用户名
            String username = JwtUtil.parseToken(token);
            request.setAttribute("username", username);
            return true;
        } catch (Exception e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "无效的token");
            return false;
        }
    }
}
