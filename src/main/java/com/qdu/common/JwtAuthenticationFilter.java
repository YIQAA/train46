package com.qdu.common;

import com.qdu.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    // 构造函数注入
    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/auth/login") || request.getRequestURI().equals("/api/user-service/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token，获取用户名
        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                String username = JwtUtil.parseToken(token);
                // 获取用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 创建认证对象
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "无效的token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}