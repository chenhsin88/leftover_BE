// src/main/java/com/example/leftovers/config/ApplicationConfig.java
package com.example.leftovers.config;

import com.example.leftovers.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class ApplicationConfig {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    // 使用建構函數注入我們需要的 DAO 和密碼編碼器
    public ApplicationConfig(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * UserDetailsService Bean
     * 告訴 Spring Security 如何根據 email 從資料庫加載使用者。
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 使用我們之前建立的 UserDao 來尋找使用者
            com.example.leftovers.entity.User userEntity = userDao.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("找不到使用者：" + username));

            // 將我們自己的 User 實體轉換成 Spring Security 需要的 UserDetails 物件
            return new User(
                userEntity.getEmail(), 
                userEntity.getPasswordHash(), 
                new ArrayList<>() // 權限列表，我們先用空的
            );
        };
    }

    /**
     * ★★★ AuthenticationProvider Bean ★★★
     * 這就是您目前缺少的 Bean。
     * 它將 UserDetailsService (如何找人) 和 PasswordEncoder (如何比對密碼) 結合在一起。
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * AuthenticationManager Bean
     * 驗證管理器，供 UserServiceImpl 呼叫以觸發登入流程。
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}