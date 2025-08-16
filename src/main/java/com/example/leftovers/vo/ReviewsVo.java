package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class ReviewsVo {

	 	private String merchant;
	 	private int merchantId;
	    private int rating;
	    private String comment;
	    private LocalDateTime createdAt;
	    private long orderId;
	    private String userName;
	    private String merchantReply;
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
		public int getMerchantId() {
			return merchantId;
		}
		public void setMerchantId(int merchantId) {
			this.merchantId = merchantId;
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
