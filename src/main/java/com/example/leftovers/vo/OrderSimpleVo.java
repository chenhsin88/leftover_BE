package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class OrderSimpleVo {
	private long orderId;
	private LocalDateTime orderAt;

	private String userName;
	private int totalAmount;
	private String status;
	private String pickupCode;

	public OrderSimpleVo() {
	}

	public OrderSimpleVo(long orderId, String userName, int totalAmount, LocalDateTime orderAt, String status,
			String pickupCode) {
		this.orderId = orderId;
		this.userName = userName;
		this.totalAmount = totalAmount;
		this.orderAt = orderAt;
		this.status = status;
		this.pickupCode = pickupCode;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderAt() {
		return orderAt;
	}

	public void setOrderAt(LocalDateTime orderAt) {
		this.orderAt = orderAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPickupCode() {
		return pickupCode;
	}

	public void setPickupCode(String pickupCode) {
		this.pickupCode = pickupCode;
	}

}
