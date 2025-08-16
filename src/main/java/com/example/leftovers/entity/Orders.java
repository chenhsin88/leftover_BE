package com.example.leftovers.entity;

import java.time.LocalDateTime;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.leftovers.vo.OrderPk;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
@IdClass(value = OrderPk.class)
public class Orders {

	@Column(name = "user_name")
	private String userName;

	@Column(name = "merchants_id")
	private int merchantsId;

	@Id
	@Column(name = "order_id")
	private long orderId;

	@Column(name = "total_amount")
	private int totalAmount;

	@Column(name = "original_total_amount")
	private int originalTotalAmount;

	@Column(name = "status")
	private String status;// 訂單狀態

	@Column(name = "payment_method_simulated")
	private String paymentMethodSimulated;// 付款方式

	@Id
	@Column(name = "pickup_code")
	private String pickupCode;// 取餐碼

	@Column(name = "notes_to_merchant")
	private String notesToMerchant;// 備註給商家

	@CreationTimestamp
	@Column(name = "ordered_at")
	private LocalDateTime orderedAt;// 下單時間

	@Column(name = "pickup_confirmed_at")
	private LocalDateTime pickupConfirmedAt;// 取餐時間

	@Column(name = "cancelled_at")
	private LocalDateTime cancelledAt;// 取消時間 (用不到)

	@Column(name = "cancellation_reason")
	private String cancellationReason;// 取消原因

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "food_id")
	private int foodId;
	
	@Column(name = "cancel_status")
	private String cancelStatus;
	
	@Column(name = "user_email")
	private String userEmail;
	
	@Column(name = "reject_reason")
	private String rejectReason;

	@Column(name = "merchant")
	private String merchant;
	
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(int merchantsId) {
		this.merchantsId = merchantsId;
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

	public LocalDateTime getPickupConfirmedAt() {
		return pickupConfirmedAt;
	}

	public void setPickupConfirmedAt(LocalDateTime pickupConfirmedAt) {
		this.pickupConfirmedAt = pickupConfirmedAt;
	}

	public LocalDateTime getCancelledAt() {
		return cancelledAt;
	}

	public void setCancelledAt(LocalDateTime cancelledAt) {
		this.cancelledAt = cancelledAt;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public String getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	

}
