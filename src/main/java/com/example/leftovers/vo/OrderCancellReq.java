package com.example.leftovers.vo;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderCancellReq {

	@Min(value = 1, message = "orderId不能為0或負數")
	private long orderId;

	@NotBlank(message = "pickupCode不能為空")
	private String pickupCode;

	@NotBlank(message = "取消原因不能空")
	private String cancellationReason;
	
	
	
	// Constructors
	public OrderCancellReq() {
	}

	public OrderCancellReq(long orderId, String pickupCode, String cancellationReason) {
		this.orderId = orderId;
		this.pickupCode = pickupCode;
		this.cancellationReason = cancellationReason;
		
	}

	// Getters & Setters
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

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}
	
}
