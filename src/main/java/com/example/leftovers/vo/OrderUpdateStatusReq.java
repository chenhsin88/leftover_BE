package com.example.leftovers.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderUpdateStatusReq {

	@Min(value = 1, message = "orderId不能為0或負數")
	private long orderId;

	@NotBlank(message = "pickupCode不能為空")
	private String pickupCode;
	
	@NotBlank(message = "更新狀態要給")
	private String status;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPickupCode() {
		return pickupCode;
	}

	public void setPickupCode(String pickupCode) {
		this.pickupCode = pickupCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
