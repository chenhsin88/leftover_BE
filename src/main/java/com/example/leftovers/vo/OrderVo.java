package com.example.leftovers.vo;

import java.time.LocalDateTime;
import java.util.List;

public class OrderVo {

	private String userName;
	
	private long orderId;
	
	private int totalAmount;
	
	private String status;
	
	private String paymentMethodSimulated;
	
	private String pickupCode;
	
	private String notesToMerchant;
	
	private LocalDateTime orderedAt;
	
	//取消
	private String rejectReason;
	
	private String cancellationReason;
	
	private String cancelStatus;
	
	//private LocalDateTime cancelledAt;
	
	//private LocalDateTime rejectAt;
	
	
	
	private int foodId;
	
	
	private List<OrderFoodItemVo> orderFoodItemList;
	
	private OrderGetMerchantsVo merchantInfo;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
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

	public String getPickupCode() {
		return pickupCode;
	}

	public void setPickupCode(String pickupCode) {
		this.pickupCode = pickupCode;
	}

	public String getNotesToMerchant() {
		return notesToMerchant;
	}

	public void setNotesToMerchant(String notesToMerchant) {
		this.notesToMerchant = notesToMerchant;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public List<OrderFoodItemVo> getOrderFoodItemList() {
		return orderFoodItemList;
	}

	public void setOrderFoodItemList(List<OrderFoodItemVo> orderFoodItemList) {
		this.orderFoodItemList = orderFoodItemList;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public OrderGetMerchantsVo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(OrderGetMerchantsVo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}

	
	
}
