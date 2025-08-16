package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class CartsUpdateReq {

	private String userEmail; // 使用者 Email

//	private int merchantId; // 商家 ID

//	private String status; // 購物車狀態

	private LocalDateTime createdAt; // 建立時間

	private LocalDateTime updatedAt; // 更新時間

	private int foodItemId; // 餐點 ID

	private int quantity; // 數量

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

//	public int getMerchantId() {
//		return merchantId;
//	}
//
//	public void setMerchantId(int merchantId) {
//		this.merchantId = merchantId;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getFoodItemId() {
		return foodItemId;
	}

	public void setFoodItemId(int foodItemId) {
		this.foodItemId = foodItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
