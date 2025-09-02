package com.maxinhai.platform.utils;

import com.maxinhai.platform.po.LoginUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName：JwtUtils
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:25
 * @Description: Jwt工具类
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * 密钥
     */
    @Value("${platform.jwt.secret}")
    private String jwtSecret;

    /**
     * 过期时间，单位毫秒
     */
    @Value("${platform.jwt.expireTime}")
    private int jwtExpireTime;

    // 生成JWT令牌
    public String generateJwtToken(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(loginUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpireTime))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // 从JWT令牌中获取用户名
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    // 验证JWT令牌
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
