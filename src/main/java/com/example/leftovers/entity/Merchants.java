package com.example.leftovers.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
@Entity
@Table(name = "merchants")
public class Merchants {

	@Id
	@Column(name = "merchants_id")
	private int merchantsId;// 商業編號
	@Column(name = "name")
	private String name;// 店家名稱
	@Column(name = "created_by_email")
	private String createdByEmail;// 註冊者的信箱
	@Column(name = "description")
	private String description;// 店家描述
	@Column(name = "address_text")
	private String addressText;// 店家地址
	@Column(name = "phone_number")
	private String phoneNumber;// 店家電話
	@Column(name = "contact_email")
	private String contactEmail;// 店家聯絡信箱
	@Column(name = "opening_hours_description")
	private String opening_hoursDescription;// 營業時間描述
	@Column(name = "is_active")
	private boolean isActive;// 是否啟用
	@Column(name = "approved_by_admin_id")
	private int approvedByAdminId;// 審核此店家的管理員 ID
	@Column(name = "approved_at")
	private LocalDateTime approvedAt;// 審核通過時間
	@Column(name = "logo_url")
	private String logoUrl;// 商家 Logo 圖片網址
	@Column(name = "banner_image_url")
	private String bannerImageUrl;// 商家橫幅圖片
	@Column(name = "map_screenshot_url")
	private String mapScreenshotUrl;// 商家地圖截圖圖片
	@Column(name = "map_google_url")
	private String mapGoogleUrl;// Google 地圖位置圖網址
	@Column(name = "pickup_instructions")
	private String pickupInstructions; // 取餐說明
	@Column(name = "longitude_and_latitude")
	private String longitudeAndLatitude; // 經緯度
	
	@Transient // 這個註解表示此欄位不對應到資料庫的真實欄位
	private List<FoodItems> foodList;

	public List<FoodItems> getFoodList() {
	    return foodList;
	}

	public void setFoodList(List<FoodItems> foodList) {
	    this.foodList = foodList;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

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

	public String getOpening_hoursDescription() {
		return opening_hoursDescription;
	}

	public void setOpening_hoursDescription(String opening_hoursDescription) {
		this.opening_hoursDescription = opening_hoursDescription;
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

	public LocalDateTime getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(LocalDateTime approvedAt) {
		this.approvedAt = approvedAt;
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

	public String getLongitudeAndLatitude() {
		return longitudeAndLatitude;
	}

	public void setLongitudeAndLatitude(String longitudeAndLatitude) {
		this.longitudeAndLatitude = longitudeAndLatitude;
	}

}
