package com.example.leftovers.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewsCreateReq {

	@NotBlank(message = "商家名稱不能空")
	private String merchants;
	
	@NotNull(message = "評分不能空")
	@Min(value = 1, message = "評分最少要1分")
	@Max(value = 5, message = "評分最多只能五分")
	private int rating;

	@NotNull(message = "訂單ID不能空")
	private long orderId;
	
	@NotBlank(message = "評論者不能空")
	private String userName;
	
	private String comment;

	private int merchantId;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMerchants() {
		return merchants;
	}

	public void setMerchants(String merchants) {
		this.merchants = merchants;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
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

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	
}
