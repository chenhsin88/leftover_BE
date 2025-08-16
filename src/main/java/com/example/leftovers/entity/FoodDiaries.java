package com.example.leftovers.entity;

import java.time.LocalDateTime;

import com.example.leftovers.constants.VisibilityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_diaries")
public class FoodDiaries {


	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "title")
	private String title;
	@Column(name = "content_text")
	private String contentText;
	@Column(name = "image_url")
	private String imageUrl;
	@Column(name = "associated_merchant_id")
	private int associatedMerchantId;
	@Column(name = "visibility")
	@Enumerated(EnumType.STRING)
	private VisibilityType visibility;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
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

	public int getAssociatedMerchantId() {
		return associatedMerchantId;
	}

	public void setAssociatedMerchantId(int associatedMerchantId) {
		this.associatedMerchantId = associatedMerchantId;
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
