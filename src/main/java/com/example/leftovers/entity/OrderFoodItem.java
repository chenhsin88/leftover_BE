package com.example.leftovers.entity;



import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import jakarta.persistence.Table;

@Entity
@Table(name = "order_food_item") // 或你實際資料表的名稱
public class OrderFoodItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 自動生成主鍵
	@Column(name = "id")
	private int id;
	
	@Column(name = "merchants_id")
	private int merchantsId;

	@Column(name = "order_id")
	private long orderId;

	
	@Column(name = "food_id")
	private int foodId;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "food_name")
	private String foodName;
	
	@Column(name = "food_price")
	private int foodPrice;
	
	@Column(name = "merchant")
	private String merchant;
	
	@CreationTimestamp
	@Column(name = "ordered_at")
	private LocalDateTime orderedAt;
	
	

	

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public int getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(int merchantsId) {
		this.merchantsId = merchantsId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public int getFoodPrice() {
		return foodPrice;
	}

	public void setFoodPrice(int foodPrice) {
		this.foodPrice = foodPrice;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	

	
}
