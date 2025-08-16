package com.example.leftovers.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
@IdClass(value = ReviewsPk.class)
public class Reviews {
	
	@Column(name = "merchant")
	private String merchant;
	
	@Column(name = "rating")
	private int rating;
	
	@Column(name = "comment")
	private String comment;
	
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Id
	@Column(name = "order_id")
	private long orderId;
	
	@Id
	@Column(name = "user_name")
	private String userName;
	
	@Id
	@Column(name = "merchant_id")
	private int merchantId;
	
	@Column(name = "merchant_reply")
	private String merchantReply;
	
	
	@Column(name = "merchant_reply_at")
	private LocalDateTime merchantReplyAt;
	

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
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

	public String getMerchantReply() {
		return merchantReply;
	}

	public void setMerchantReply(String merchantReply) {
		this.merchantReply = merchantReply;
	}

	public LocalDateTime getMerchantReplyAt() {
		return merchantReplyAt;
	}

	public void setMerchantReplyAt(LocalDateTime merchantReplyAt) {
		this.merchantReplyAt = merchantReplyAt;
	}
	
	
}
