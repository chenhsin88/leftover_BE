package com.example.leftovers.vo;

import java.time.LocalDateTime;
import java.util.List;

public class HistoryGetUserAllOrderVo {
	
	private String userEmail;
//	private String userName;
	
	private String orderId;
    private int unitPrice;
    private LocalDateTime createdAt;
    private String status;
    private int merchantId;
    private String merchantName;
    
    private List<HistoryOrderItemVo> items; // 商品清單
    




	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}




	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}



	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<HistoryOrderItemVo> getItems() {
		return items;
	}

	public void setItems(List<HistoryOrderItemVo> items) {
		this.items = items;
	}


    
}
