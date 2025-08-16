// 新增檔案到 vo 套件: src/main/java/com/example/leftovers/vo/ReviewVo.java
package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class ReviewVo {

    private int rating;
    private String comment;
    private String userName;
    private LocalDateTime createdAt;
    private String merchantReply;
    private LocalDateTime merchantReplyAt;
    private String profilePictureUrl;
    
    public ReviewVo(int rating, String comment, String userName, LocalDateTime createdAt, String merchantReply, LocalDateTime merchantReplyAt, String profilePictureUrl) {
        this.rating = rating;
        this.comment = comment;
        this.userName = userName;
        this.createdAt = createdAt;
        this.merchantReply = merchantReply;
        this.merchantReplyAt = merchantReplyAt;
        this.profilePictureUrl = profilePictureUrl;
    }

    public ReviewVo() { } // 保留無參數建構子
    // Getters and Setters
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMerchantReply() {
        return merchantReply;
    }

    public void setMerchantReply(String merchantReply) {
        this.merchantReply = merchantReply;
    }

    public LocalDateTime getMerchantReplyAt() {
        return merchantReplyAt;
    }

    public void setMerchantReplyAt(LocalDateTime merchantReplyAt) {
        this.merchantReplyAt = merchantReplyAt;
    }
}