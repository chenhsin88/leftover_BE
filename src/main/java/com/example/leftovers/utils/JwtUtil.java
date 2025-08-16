// src/main/java/com/example/leftovers/utils/JwtUtil.java
package com.example.leftovers.utils;

import com.auth0.jwt.JWT;
import java.util.stream.Collectors; // 引入這個
import org.springframework.security.core.GrantedAuthority;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.leftovers.config.filter.JwtAuthFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// ★★★ 使用 Auth0 JWT API 重寫 ★★★
@Component
public class JwtUtil {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    // --- Token 生成 ---
    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshTokenExpiration);
    }

    private String buildToken(UserDetails userDetails, long expiration) {
    	Date expirationDate = new Date(System.currentTimeMillis() + expiration);
    	logger.info("正在生成新的 Token，主題: {}, 過期時間: {}", userDetails.getUsername(), expirationDate);
    	
    	List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    	
        // 使用 Auth0 的語法來建立 JWT
        return JWT.create()
                .withSubject(userDetails.getUsername()) // 設定主題，通常是 email
                .withIssuedAt(new Date(System.currentTimeMillis())) // 設定簽發時間
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration)) // 設定過期時間
                .withClaim("roles", authorities) // 設定使用者角色
                .sign(Algorithm.HMAC256(secretKey)); // 使用 HMAC256 演算法和密鑰進行簽名
    }

    // --- Token 驗證與解析 ---
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // 檢查 token 中的使用者名稱是否與 UserDetails 相符，且 token 未過期
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        // 如果過期時間在當前時間之前，代表已過期
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        // 從 token 中解析出 Subject (使用者名稱)
        return getDecodedJWT(token).getSubject();
    }

    private Date extractExpiration(String token) {
        // 從 token 中解析出過期時間
        return getDecodedJWT(token).getExpiresAt();
    }

    // 輔助方法：解析 token 字串為 DecodedJWT 物件
    private DecodedJWT getDecodedJWT(String token) {
        // 建立一個驗證器，指定演算法和密鑰
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        // 驗證並解碼 token
        return verifier.verify(token);
    }
}