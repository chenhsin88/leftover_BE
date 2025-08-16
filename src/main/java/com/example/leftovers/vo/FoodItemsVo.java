package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class FoodItemsVo {

	private int id;
	private int merchantsId;
	private String name;
	private String description;
	private String imageUrl;
	private int originalPrice;
	private int discountedPrice;
	private int quantityAvailable;
	private LocalDateTime pickupStartTime;
	private LocalDateTime pickupEndTime;
	private String pickupInstructions;
	private String Category;
	private boolean Active;
	private LocalDateTime createdAt;
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

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
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

	public String getPickupInstructions() {
		return pickupInstructions;
	}

	public void setPickupInstructions(String pickupInstructions) {
		this.pickupInstructions = pickupInstructions;
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
