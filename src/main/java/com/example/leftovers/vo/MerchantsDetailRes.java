// 新增檔案到 vo 套件: src/main/java/com/example/leftovers/vo/MerchantsDetailRes.java
package com.example.leftovers.vo;

import java.util.List;
import com.example.leftovers.entity.Merchants;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantsDetailRes extends BasicRes {

    private Merchants merchant;
    private double averageRating;
    private int reviewCount;
    private List<ReviewVo> reviews;

    public MerchantsDetailRes(int code, String message) {
        super(code, message);
    }

    // Getters and Setters
    public Merchants getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchants merchant) {
        this.merchant = merchant;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<ReviewVo> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewVo> reviews) {
        this.reviews = reviews;
    }
}