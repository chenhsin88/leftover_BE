package com.example.leftovers.exception;

// 這個例外專門用來表示登入失敗、帳密錯誤等情況
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}