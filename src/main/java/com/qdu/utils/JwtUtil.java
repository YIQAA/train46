package com.qdu.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// JWT 工具类，用于生成和验证 JWT
@Component
public class JwtUtil {

    // 生成 512 位（64 字节）的密钥
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 获取 Base64 编码后的密钥字符串
    private static final String BASE64_SECRET_KEY = Encoders.BASE64.encode(SECRET_KEY.getEncoded());
    private static final long EXPIRATION_TIME = 86400000; // 24小时

    // 生成 JWT
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, BASE64_SECRET_KEY)
                .compact();
    }

    // 解析 JWT
    public static String parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(BASE64_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}