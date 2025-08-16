package com.example.leftovers.vo;

import java.time.LocalDateTime;

import com.example.leftovers.constants.ConstantsMessage;


import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MerchantsReq {

	@Id
	@NotNull
//	@Min(value = 1, message = ConstantsMessage.PARAM_MERCHANTS_ID_ERROR)
	private int merchantsId;
	
	@NotBlank(message = ConstantsMessage.PARAM_NAME_ERROR)
	private String name;// 店家名稱

	@NotBlank(message = ConstantsMessage.PARAM_MERCHANT_DESCRIPTION_FORMAT_ERROR)
	private String description;// 店家描述

	@Pattern(regexp = "^(台北市|新北市|桃園市|台中市|台南市|高雄市|基隆市|新竹市|嘉義市|新竹縣|苗栗縣|彰化縣|南投縣|雲林縣|嘉義縣|屏東縣|宜蘭縣|花蓮縣|台東縣|澎湖縣|金門縣|連江縣)"
			+ "[\\u4e00-\\u9fa5]{1,6}(市區|區|鄉|鎮)" + "[\\u4e00-\\u9fa5]{1,12}(路|街|大道)?(一|二|三|四|五|六|七|八|九|十)?段?"
			+ "(\\d+巷)?(\\d+弄)?\\d+號" + "(\\d+樓)?(之\\d+)?$", message = "地址格式有誤!!(例:台北市大安區信義路三段88號12樓之1)")
	@NotBlank(message = ConstantsMessage.PARAM_ADDRESS_FORMAT_ERROR)
	private String addressText;// 店家地址

	@Pattern(regexp = "^(0\\d{1,2}-\\d{3,4}-\\d{4}|09\\d{2}-?\\d{3}-?\\d{3})$", message = "聯絡電話格式錯誤，若市話號碼要區碼，若手機格式則09開頭共10位數字")
	@NotBlank(message = ConstantsMessage.PARAM_PHONE_NUMBER_ERROR)
	private String phoneNumber;// 店家電話

	@NotBlank(message = ConstantsMessage.PARAM_EMAIL_ERROR)
	private String contactEmail;// 店家聯絡信箱
	
	@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$", message = "信箱格式不正確")
	@NotBlank(message = ConstantsMessage.PARAM_EMAIL_ERROR)
	private String createdByEmail;// 管理員信箱
//	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,16}$", //
//			message = "密碼長度必須為6~16字元，且包含大小寫英文及數字")
//	@NotBlank(message = ConstantsMessage.PARAM_PASSWORDHASH_ERROR)
//	private String passwordHash;// 登入密碼

	@NotBlank(message = ConstantsMessage.PARAM_OPENING_HOURS_FORMAT_ERROR)
	private String openingHoursDescription;// 營業時間描述

	private LocalDateTime approvedAt;// 審核通過時間

	private boolean isActive;// 是否啟用

	private int approvedByAdminId;// 審核此店家的管理員 ID

	private String logoUrl;// 商家 Logo 圖片網址

	private String bannerImageUrl;// 商家橫幅圖片

	private String mapScreenshotUrl;// 商家地圖截圖圖片

	private String mapGoogleUrl;// Google 地圖位置圖網址
	
	@NotBlank(message = "取餐說明未填寫!!")
	private String pickupInstructions; //取餐說明
	
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

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	public String getOpeningHoursDescription() {
		return openingHoursDescription;
	}

	public void setOpeningHoursDescription(String openingHoursDescription) {
		this.openingHoursDescription = openingHoursDescription;
	}

	public LocalDateTime getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(LocalDateTime approvedAt) {
		this.approvedAt = approvedAt;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getApprovedByAdminId() {
		return approvedByAdminId;
	}

	public void setApprovedByAdminId(int approvedByAdminId) {
		this.approvedByAdminId = approvedByAdminId;
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

	public String getPickupInstructions() {
		return pickupInstructions;
	}

	public void setPickupInstructions(String pickupInstructions) {
		this.pickupInstructions = pickupInstructions;
	}


}
