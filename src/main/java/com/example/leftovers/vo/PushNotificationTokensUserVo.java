package com.example.leftovers.vo;

public class PushNotificationTokensUserVo {
	// 前台推播區畫面要抓的東西，商品圖片、商品名稱、商品描述、原價、折扣後價格、再折扣後價格、取貨時間、商家名稱、商家地址、商家ID、食物ID、距離
	// 前台推播區畫面要抓的東西
	private int merchantId; // 商家ID
	private int foodItemId; // 食物ID

	private String merchantName; // 商家名稱
	private String merchantAddress; // 商家地址
	private int distance; // 距離
	private String category; // 商品類別
	private String foodItemName; // 商品名稱
	private String foodItemDescription;// 商品描述
	private String foodItemImageUrl; // 商品圖片（建議用URL）

	private int originalPrice; // 原價
	private int discountedPrice; // 折扣後價格
	private int finalPrice; // 再折扣後價格（最終價格）

	private String pickupTime; // 取貨時間（可改為Date類型，視需求而定）

	public PushNotificationTokensUserVo(int merchantId, int foodItemId, String merchantName, String merchantAddress,
			int distance, String category, String foodItemName, String foodItemDescription, String foodItemImageUrl,
			int originalPrice, int discountedPrice, int finalPrice, String pickupTime) {
		super();
		this.merchantId = merchantId;
		this.foodItemId = foodItemId;
		this.merchantName = merchantName;
		this.merchantAddress = merchantAddress;
		this.distance = distance;
		this.category = category;
		this.foodItemName = foodItemName;
		this.foodItemDescription = foodItemDescription;
		this.foodItemImageUrl = foodItemImageUrl;
		this.originalPrice = originalPrice;
		this.discountedPrice = discountedPrice;
		this.finalPrice = finalPrice;
		this.pickupTime = pickupTime;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getFoodItemId() {
		return foodItemId;
	}

	public void setFoodItemId(int foodItemId) {
		this.foodItemId = foodItemId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFoodItemName() {
		return foodItemName;
	}

	public void setFoodItemName(String foodItemName) {
		this.foodItemName = foodItemName;
	}

	public String getFoodItemDescription() {
		return foodItemDescription;
	}

	public void setFoodItemDescription(String foodItemDescription) {
		this.foodItemDescription = foodItemDescription;
	}

	public String getFoodItemImageUrl() {
		return foodItemImageUrl;
	}

	public void setFoodItemImageUrl(String foodItemImageUrl) {
		this.foodItemImageUrl = foodItemImageUrl;
	}

	public int getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(int discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public int getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(int finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}


}
