package com.example.leftovers.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "push_notification_tokens")
public class PushNotificationTokens {

	@Id
	@Column(name = "food_items_id")
	private int foodItemsId;
	@Column(name = "merchants_id")
	private int merchantsId;
	@Column(name = "previous_price")
	private int previousPrice;
	@Column(name = "new_price")
	private int newPrice;
	@Column(name = "default_hours")
	private int defaultHours;
	@Column(name = "set_time")
	private LocalDateTime setTime;
	@Column(name = "push_notifications")
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
	public int getPreviousPrice() {
		return previousPrice;
	}
	public void setPreviousPrice(int previousPrice) {
		this.previousPrice = previousPrice;
	}
	public int getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(int newPrice) {
		this.newPrice = newPrice;
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
