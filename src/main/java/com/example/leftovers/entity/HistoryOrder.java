package com.example.leftovers.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders_history")
public class HistoryOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 自動生成主鍵
	@Column(name = "id")
	private int id;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "food_id")
	private int foodId;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "unit_price")
	private int unitPrice;

	@Column(name = "food_price")
	private int foodPrice;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "food_name")
	private String foodName;

	@Column(name = "merchant")
	private String merchant;

	@Column(name = "merchant_id")
	private int merchantId;
	
	@Column(name = "user_email")
	private String userEmail;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "pickup_code")
	private String pickupCode;
	
	@Column(name = "notes_to_merchant")
	private String notesToMerchant;
	
	@Column(name = "payment_method_simulated")
	private String paymentMethodSimulated;
	
	@Column(name = "cancellation_reason")
	private String cancellationReason;
	
	@Column(name = "reject_reason")
	private String rejectReason;
	
	@Column(name = "status")
	private String status;
	

	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFoodPrice() {
		return foodPrice;
	}

	public void setFoodPrice(int foodPrice) {
		this.foodPrice = foodPrice;
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

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPaymentMethodSimulated() {
		return paymentMethodSimulated;
	}

	public void setPaymentMethodSimulated(String paymentMethodSimulated) {
		this.paymentMethodSimulated = paymentMethodSimulated;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
