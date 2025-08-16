package com.example.leftovers.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class DiagnosticFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 只針對我們關心的預檢請求 OPTIONS 進行詳細日誌記錄
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("\n\n--- DIAGNOSTIC FILTER START ---");
            System.out.println("Received an OPTIONS preflight request for URL: " + request.getRequestURL());
            System.out.println("Request Method: " + request.getMethod());

            // 印出所有請求標頭
            Collections.list(request.getHeaderNames()).forEach(headerName ->
                    System.out.println("Header: " + headerName + " = " + request.getHeader(headerName))
            );

            System.out.println("--- DIAGNOSTIC FILTER END ---\n\n");
        }

        // ★★★ 關鍵：無論如何都要繼續執行過濾鏈，否則請求會卡住 ★★★
        filterChain.doFilter(request, response);
    }
}