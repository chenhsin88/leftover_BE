package com.example.leftovers.vo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrdersCreateReq {

	@NotBlank(message = "使用者名稱不能空")
	private String userName;
	
	@NotNull(message = "商家Id不能空")
	private int merchantId;
	
//	@NotNull(message = "訂單編號不能空")
//	private int orderId;
	
	@NotNull(message = "折後總價格不能空")
	private int totalAmount;
	
	
	private int originalTotalAmount;
	
	@NotBlank(message = "訂單狀態不能空")
	private String status;
	
	@NotBlank(message = "付款方式不能空")
	private String paymentMethodSimulated;
	
//	@NotBlank(message = "取餐碼不能空")
//	private String pickupCode;
	
	private String notesToMerchant; //備註
	
//	@NotNull(message = "取餐時間不能空")
//	private LocalDateTime pickupConfirmedAt;
	
	@NotNull(message = "訂單餐點不能空")
	private List<OrderItemReq> orderItems;
	
	@NotBlank(message = "使用者email不能空")
	private String userEmail;
	
	private String merchant;

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

//	public int getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(int orderId) {
//		this.orderId = orderId;
//	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getOriginalTotalAmount() {
		return originalTotalAmount;
	}

	public void setOriginalTotalAmount(int originalTotalAmount) {
		this.originalTotalAmount = originalTotalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentMethodSimulated() {
		return paymentMethodSimulated;
	}

	public void setPaymentMethodSimulated(String paymentMethodSimulated) {
		this.paymentMethodSimulated = paymentMethodSimulated;
	}

//	public String getPickupCode() {
//		return pickupCode;
//	}
//
//	public void setPickupCode(String pickupCode) {
//		this.pickupCode = pickupCode;
//	}

	public String getNotesToMerchant() {
		return notesToMerchant;
	}

	public void setNotesToMerchant(String notesToMerchant) {
		this.notesToMerchant = notesToMerchant;
	}

//	public LocalDateTime getPickupConfirmedAt() {
//		return pickupConfirmedAt;
//	}
//
//	public void setPickupConfirmedAt(LocalDateTime pickupConfirmedAt) {
//		this.pickupConfirmedAt = pickupConfirmedAt;
//	}

	public List<OrderItemReq> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemReq> orderItems) {
		this.orderItems = orderItems;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	
	
	
}
