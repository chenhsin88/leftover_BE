// src/main/java/com/example/leftovers/config/filter/JwtAuthFilter.java
package com.example.leftovers.config.filter;
import com.auth0.jwt.exceptions.JWTVerificationException; // 引入基礎的驗證例外
import com.auth0.jwt.exceptions.TokenExpiredException;    // 引入專門的過期例外
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.leftovers.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	 private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
	private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("JwtAuthFilter is processing request for: [{}] {}", request.getMethod(), request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail;

        // ★★★ 使用 try-catch 包裹住所有 JWT 驗證相關的操作 ★★★
        try {
            userEmail = jwtUtil.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // 如果 JWT 驗證成功，或是不需要驗證，就繼續執行
            filterChain.doFilter(request, response);

        } catch (TokenExpiredException e) {
            // 【捕捉到 Token 過期的例外】
            logger.warn("JWT Token has expired: {}", e.getMessage());
            // 設定回應狀態碼為 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token has expired\"}"); // 可選：回傳一個 JSON
            // 不再執行後續的 filterChain.doFilter，直接結束
        } catch (JWTVerificationException e) {
            // 【捕捉到其他 JWT 驗證失敗的例外，例如簽名不符】
            logger.warn("JWT Verification failed: {}", e.getMessage());
            logger.error("JWT 驗證失敗！收到的 Token 是: [{}], 錯誤: {}", jwt, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid Token\"}");
        }
    }
}