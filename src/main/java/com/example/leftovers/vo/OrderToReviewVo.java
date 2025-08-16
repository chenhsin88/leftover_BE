// 檔案路徑: src/main/java/com/example/leftovers/vo/OrderToReviewVo.java
package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class OrderToReviewVo {

    private long numericOrderId; // 純數字的訂單ID，給前端提交評論時用
    private String displayOrderId; // 顯示用的訂單ID，例如 "RF202307110123"
    private String merchantName; // 商家名稱
    private LocalDateTime orderDate; // 下單日期
    private String foodName; // 商品名稱 (您歷史訂單中的 food_name)

    // 建構子 (Constructor)
    public OrderToReviewVo(long numericOrderId, String displayOrderId, String merchantName, LocalDateTime orderDate, String foodName) {
        this.numericOrderId = numericOrderId;
        this.displayOrderId = displayOrderId;
        this.merchantName = merchantName;
        this.orderDate = orderDate;
        this.foodName = foodName;
    }

    // Getters and Setters
    public long getNumericOrderId() { return numericOrderId; }
    public void setNumericOrderId(long numericOrderId) { this.numericOrderId = numericOrderId; }
    public String getDisplayOrderId() { return displayOrderId; }
    public void setDisplayOrderId(String displayOrderId) { this.displayOrderId = displayOrderId; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
}