package com.qdu.controller;

import com.qdu.dto.req.LoginDTO;
import com.qdu.entity.Users;
import com.qdu.service.impl.UsersServiceImpl;
import com.qdu.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
/**
 * <p>
 * 登录 前端控制器
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersServiceImpl userService;

    private final AuthenticationManager authenticationManager;
    // 通过构造函数注入
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 登录，返回token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        System.out.println(passwordEncoder.encode("123"));

        try {
            // 1. 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // 2. 获取用户详细信息
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("用户详细信息"+userDetails);


            // 3. 生成 Token
            String token = JwtUtil.generateToken(userDetails.getUsername());

            Users user = userService.getOneByUsername(userDetails.getUsername());
            // 4. 构造响应数据（包含用户信息）
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("username",userDetails.getUsername());
            responseData.put("userId",user.getUserId());
            responseData.put("userRole",user.getRole());
            responseData.put("realName",user.getRealName());

            return ResponseEntity.ok().body(responseData);
            //return ResponseEntity.ok().body(Collections.singletonMap("token", token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }

    }
}