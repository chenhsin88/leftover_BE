package com.example.leftovers.vo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PushNotificationTokensUserDto {
	private int foodItemsId;
	private String foodName;
	private String category;
	private String description;
	private double originalPrice;
	private double discountedPrice;
	private String imageUrl;
	private LocalDateTime setTime; // 內部用 LocalDateTime
	private LocalDateTime pickupTime; // 內部用 LocalDateTime
	private Integer newPrice;
	private int merchantsId;
	private String merchantName;
	private String addressText;
	private String longitudeAndLatitude;

	// 建構子改用 Timestamp 參數，並轉成 LocalDateTime
	public PushNotificationTokensUserDto(
			int foodItemsId, 
			String foodName, 
			String category, 
			String description,
			double originalPrice, 
			double discountedPrice, 
			String imageUrl, 
			Timestamp setTime, 
			Timestamp pickupTime,
			Integer newPrice, 
			int merchantsId, 
			String merchantName, 
			String addressText, 
			String longitudeAndLatitude
			) {
		this.foodItemsId = foodItemsId;
		this.foodName = foodName;
		this.category = category;
		this.description = description;
		this.originalPrice = originalPrice;
		this.discountedPrice = discountedPrice;
		this.imageUrl = imageUrl;
		this.setTime = (setTime != null) ? setTime.toLocalDateTime() : null;
		this.pickupTime = (pickupTime != null) ? pickupTime.toLocalDateTime() : null;
		this.newPrice = newPrice;
		this.merchantsId = merchantsId;
		this.merchantName = merchantName;
		this.addressText = addressText;
		this.longitudeAndLatitude = longitudeAndLatitude;
	}

	// 以下是 getter 和 setter（可用 IDE 生成）

	public int getFoodItemsId() {
		return foodItemsId;
	}

	public void setFoodItemsId(int foodItemsId) {
		this.foodItemsId = foodItemsId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LocalDateTime getSetTime() {
		return setTime;
	}

	public void setSetTime(LocalDateTime setTime) {
		this.setTime = setTime;
	}

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(LocalDateTime pickupTime) {
		this.pickupTime = pickupTime;
	}

	public Integer getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(Integer newPrice) {
		this.newPrice = newPrice;
	}

	public int getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(int merchantsId) {
		this.merchantsId = merchantsId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getAddressText() {
		return addressText;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

	public String getLongitudeAndLatitude() {
		return longitudeAndLatitude;
	}

	public void setLongitudeAndLatitude(String longitudeAndLatitude) {
		this.longitudeAndLatitude = longitudeAndLatitude;
	}
}
