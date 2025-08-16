package com.example.leftovers.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue; // 【請新增此 import】
import jakarta.persistence.GenerationType; // 【請新增此 import】
@Entity
@Table(name = "carts")
public class Carts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id; // 主鍵

	@Column(name = "user_email")
	private String userEmail; // 使用者 ID

	@Column(name = "merchants_id")
	private int merchantsId; // 商家 ID

	@Column(name = "status")
	private String status; // 購物車狀態

	@Column(name = "created_at")
	private LocalDateTime createdAt; // 建立時間

	@Column(name = "updated_at")
	private LocalDateTime updatedAt; // 更新時間

	@Column(name = "food_item_id")
	private int foodItemId; // 餐點 ID

	@Column(name = "quantity")
	private int quantity; // 數量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(int merchantsId) {
		this.merchantsId = merchantsId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
