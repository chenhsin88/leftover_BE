package com.example.leftovers.config;

import java.time.Duration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.leftovers.config.filter.JwtAuthFilter;
import org.springframework.web.filter.CorsFilter; // ★★★【新增這個 import】★★★
import com.example.leftovers.config.filter.DiagnosticFilter; 
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) throws Exception {

        // 允許公開存取的 GET 請求路徑 (任何人都可以讀取)
        final String[] PUBLIC_GET_ROUTES = {
            "/merchants/all",
            "/merchants/getMerchantsData/**",
            "/merchants/detail/**",
            "/fooditems/getAll",
            "/fooditems/getAllByMerchantId",
            "/reviews/merchant",
            "/users/checkEmailExists/**",
            "/faq/**",
            "/sse/**",
            "/api/payment/public-url",
            "/favicon.ico",
            "/orders/**",
            "/historyOrder/**",
            "/carts/**",
        };

        // 允許公開存取的 POST 請求路徑 (任何人都可以發起)
        final String[] PUBLIC_POST_ROUTES = {
            "/users/register",
            "/users/login",
            "/users/refresh",
            "/merchants/update",
            "/merchants/getMerchantsDistance",
            "/merchants/getMerchantsWithinRange",
            "/merchants/getMerchantAndFoodItemsWithinRange",
            "/merchants/getLightweightMerchantDataWithinRange",
            "/merchants/getLightweightMerchantsWithinRange",
            "/api/payment/**", // 外部金流服務回呼
            "/orders/**",
            "/historyOrder/**",
            "/carts/**",
            "/checkEmailExists/**",
        };

        http
         .addFilterBefore(new DiagnosticFilter(), CorsFilter.class)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 停用 CSRF
            .csrf(AbstractHttpConfigurer::disable)


            .authorizeHttpRequests(authz -> authz
                // 允許所有瀏覽器的 CORS 預檢請求 (Preflight)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 允許所有定義在 PUBLIC_GET_ROUTES 列表中的 GET 請求
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ROUTES).permitAll()

                // 允許所有定義在 PUBLIC_POST_ROUTES 列表中的 POST 請求
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ROUTES).permitAll()
                
//                .requestMatchers(HttpMethod.POST, "/merchants/register").hasAuthority("merchants") 
//                .requestMatchers(HttpMethod.POST, "/merchants/update").hasAuthority("merchants") // 只有商家能呼叫更新API
//                .requestMatchers(HttpMethod.POST, "/merchants/getMerchantsData").hasAuthority("merchants") // 只有商家能取得自己的店家資料
                
                // ★★★ 保護其他所有請求，要求必須經過身份驗證 ★★★
                .anyRequest().authenticated()
//                .requestMatchers("/**").permitAll() 
            )

            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // setAllowedOriginPatterns 來正確支援萬用字元
        List<String> allowedOriginPatterns = Arrays.asList(

			"http://localhost:8083", // docker
			"http://host.docker.internal:8080", // docker for mac/windows
        	"http://localhost:8080",
            "http://localhost:4200",
            "https://middlen8n.servehttp.com",
            "https://*.newebpay.com", // 涵蓋藍新金流所有子域名
            "https://*.ngrok-free.app",
            "https://a3ee3228448a.ngrok-free.app",
            "https://nominatim.openstreetmap.org",
            "https://*.carto.com",     // 涵蓋 carto.com 和所有子域名
            "https://*.cartocdn.com",  // 涵蓋 cartocdn.com 和所有子域名
            "https://bam.nr-data.net"
        );
        configuration.setAllowedOriginPatterns(allowedOriginPatterns);

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 建議 : 最好明確列出常用標頭，但暫時保留 "*" 以免破壞現有功能。
        // 未來可改為：Arrays.asList("Authorization", "Content-Type", "X-Requested-With")
        configuration.setAllowedHeaders(Arrays.asList("*"));

        configuration.setAllowCredentials(true);

        // 設定 maxAge，讓瀏覽器快取 preflight 結果一小時，減少請求量以提升效能
        configuration.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}