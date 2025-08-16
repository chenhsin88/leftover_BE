package com.example.leftovers.vo;

import java.time.LocalDateTime;

import com.example.leftovers.constants.VisibilityType;


public class FoodDiariesReq {

	private int id;
	private int userId;
	private String title;
	private String contentText;
	private String imageUrl ;
	private int associatedMerchantId;
	private VisibilityType visibility;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAssociatedMerchantId() {
		return associatedMerchantId;
	}
	public void setAssociatedMerchantId(int associatedMerchantId) {
		this.associatedMerchantId = associatedMerchantId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContentText() {
		return contentText;
	}
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public VisibilityType getVisibility() {
		return visibility;
	}
	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
