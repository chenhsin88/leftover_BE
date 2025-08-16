package com.example.leftovers.vo;

import java.time.LocalDateTime;

public class MerchantsUpdateReq {
	
	private int merchantsId;
	private String name;
	private String passwordHash;
	private String description;
	private String addressText;
	private String phoneNumber;
	private String contactEmail;
	private String CreatedByEmail;
	private String logoUrl;
	private String bannerImageUrl;
	private String openingHoursDescription;
	private String mapScreenshotUrl;
	private String mapGoogleUrl;
	private boolean isActive;
	private Integer approvedByAdminId;
	private LocalDateTime approvedAt;
	private String pickupInstructions;
	
	
	public String getPickupInstructions() {
		return pickupInstructions;
	}
	public void setPickupInstructions(String pickupInstructions) {
		this.pickupInstructions = pickupInstructions;
	}
	public int getMerchantsId() {
		return merchantsId;
	}
	public void setMerchantsId(int merchantsId) {
		this.merchantsId = merchantsId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddressText() {
		return addressText;
	}
	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCreatedByEmail() {
		return CreatedByEmail;
	}
	public void setCreatedByEmail(String createdByEmail) {
		CreatedByEmail = createdByEmail;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getBannerImageUrl() {
		return bannerImageUrl;
	}
	public void setBannerImageUrl(String bannerImageUrl) {
		this.bannerImageUrl = bannerImageUrl;
	}
	public String getOpeningHoursDescription() {
		return openingHoursDescription;
	}
	public void setOpeningHoursDescription(String openingHoursDescription) {
		this.openingHoursDescription = openingHoursDescription;
	}
	public String getMapScreenshotUrl() {
		return mapScreenshotUrl;
	}
	public void setMapScreenshotUrl(String mapScreenshotUrl) {
		this.mapScreenshotUrl = mapScreenshotUrl;
	}
	public String getMapGoogleUrl() {
		return mapGoogleUrl;
	}
	public void setMapGoogleUrl(String mapGoogleUrl) {
		this.mapGoogleUrl = mapGoogleUrl;
	}
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Integer getApprovedByAdminId() {
		return approvedByAdminId;
	}
	public void setApprovedByAdminId(Integer approvedByAdminId) {
		this.approvedByAdminId = approvedByAdminId;
	}
	public LocalDateTime getApprovedAt() {
		return approvedAt;
	}
	public void setApprovedAt(LocalDateTime approvedAt) {
		this.approvedAt = approvedAt;
	}
	
	
}
