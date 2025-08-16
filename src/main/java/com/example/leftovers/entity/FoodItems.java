package com.example.leftovers.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "food_items")
public class FoodItems {
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "merchants_id")
	private int merchantsId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "original_price")
	private int originalPrice;
	
	@Column(name = "discounted_price")
	private int discountedPrice;
	
	@Column(name = "quantity_available")
	private int quantityAvailable;
	
	@Column(name = "pickup_start_time")
	private LocalDateTime pickupStartTime;
	
	@Column(name = "pickup_end_time")
	private LocalDateTime pickupEndTime;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(int merchantsId) {
		this.merchantsId = merchantsId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(int discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public int getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public LocalDateTime getPickupStartTime() {
		return pickupStartTime;
	}

	public void setPickupStartTime(LocalDateTime pickupStartTime) {
		this.pickupStartTime = pickupStartTime;
	}

	public LocalDateTime getPickupEndTime() {
		return pickupEndTime;
	}

	public void setPickupEndTime(LocalDateTime pickupEndTime) {
		this.pickupEndTime = pickupEndTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
	
}
