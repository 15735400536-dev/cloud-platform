package com.maxinhai.platform.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 工具类，用于生成、解析和验证 Token
 */
@Component
public class JwtConfig {

    @Value("${jwt.secret:cloud-platform}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration; // 单位：毫秒

    /**
     * 从 Token 中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).getOrDefault("userId", "").toString();
    }

    /**
     * 从 Token 中获取账号
     */
    public String getAccountFromToken(String token) {
        return getAllClaimsFromToken(token).getOrDefault("account", "").toString();
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getOrDefault("username", "").toString();
    }

    /**
     * 从 Token 中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从 Token 中获取自定义声明
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 Token 获取所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 检查 Token 是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 生成 Token
     */
    public String generateToken(String userId, String account, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("account", account);
        claims.put("username", username);
        return doGenerateToken(claims, "cloud-platform");
    }

    /**
     * 生成 Token 的具体实现
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 验证 Token
     */
    public Boolean validateToken(String token, String account) {
        final String accountFromToken = getAccountFromToken(token);
        return (accountFromToken.equals(account) && !isTokenExpired(token));
    }

}
