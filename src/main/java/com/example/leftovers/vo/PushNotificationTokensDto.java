package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class PushNotificationTokensDto {

	private int foodItemId;// 食物id

	private String name;// 食物名稱

	private String imageUrl;// 食物圖片

	private int originalPrice;// 食物原價

	private int discountedPrice;// 折扣後價格

	private LocalDateTime pickupEndTime;// 結束營業時間

	// 推播的資料庫
	private Integer newPrice;// 最新折扣

	private Integer defaultHours;// 預設小時

	private LocalDateTime setTime;// 設定時間

	private boolean pushNotifications;// 推播開關

	public PushNotificationTokensDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PushNotificationTokensDto(int foodItemId, String name, String imageUrl, int originalPrice,
			int discountedPrice, LocalDateTime pickupEndTime, Integer newPrice, Integer defaultHours,
			LocalDateTime setTime, boolean pushNotifications) {
		super();
		this.foodItemId = foodItemId;
		this.name = name;
		this.imageUrl = imageUrl;
		this.originalPrice = originalPrice;
		this.discountedPrice = discountedPrice;
		this.pickupEndTime = pickupEndTime;
		this.newPrice = newPrice;
		this.defaultHours = defaultHours;
		this.setTime = setTime;
		this.pushNotifications = pushNotifications;
	}

	public int getFoodItemId() {
		return foodItemId;
	}

	public void setFoodItemId(int foodItemId) {
		this.foodItemId = foodItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public LocalDateTime getPickupEndTime() {
		return pickupEndTime;
	}

	public void setPickupEndTime(LocalDateTime pickupEndTime) {
		this.pickupEndTime = pickupEndTime;
	}

	public Integer getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(Integer newPrice) {
		this.newPrice = newPrice;
	}

	public Integer getDefaultHours() {
		return defaultHours;
	}

	public void setDefaultHours(Integer defaultHours) {
		this.defaultHours = defaultHours;
	}

	public LocalDateTime getSetTime() {
		return setTime;
	}

	public void setSetTime(LocalDateTime setTime) {
		this.setTime = setTime;
	}

	public boolean isPushNotifications() {
		return pushNotifications;
	}

	public void setPushNotifications(boolean pushNotifications) {
		this.pushNotifications = pushNotifications;
	}

}
