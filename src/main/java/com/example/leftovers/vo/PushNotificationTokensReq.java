package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class PushNotificationTokensReq {

	private int foodItemsId;

	private int merchantsId;

	private int previousPrice;

	private int newPrice;

	private int defaultHours;

	private LocalDateTime setTime;

	private boolean pushNotifications;

	public int getFoodItemsId() {
		return foodItemsId;
	}

	public void setFoodItemsId(int foodItemsId) {
		this.foodItemsId = foodItemsId;
	}

	public int getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(int merchantsId) {
		this.merchantsId = merchantsId;
	}

	public int getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(int newPrice) {
		this.newPrice = newPrice;
	}

	public int getPreviousPrice() {
		return previousPrice;
	}

	public void setPreviousPrice(int previousPrice) {
		this.previousPrice = previousPrice;
	}

	public int getDefaultHours() {
		return defaultHours;
	}

	public void setDefaultHours(int defaultHours) {
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
