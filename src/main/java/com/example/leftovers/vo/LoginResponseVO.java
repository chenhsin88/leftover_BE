package com.example.leftovers.vo;

/**
 * 這是 LoginResponseVO 的最終正確版本，不使用任何 Lombok。
 */
public class LoginResponseVO {

    private String accessToken;
    private String refreshToken;

    // 空的建構函數 (保留它有好處)
    public LoginResponseVO() {
    }

    // ★★★ 錯誤訊息所指的就是這個建構函數 ★★★
    // ★★★ 請確認您的檔案中有這一段 ★★★
    public LoginResponseVO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // --- Getters and Setters ---
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}