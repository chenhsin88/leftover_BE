package com.example.leftovers.vo;

import java.util.List;

public class CartByMerchantVo {

	private int merchantId;
	private String merchantName;
	private List<CartItemVo> foodItems;

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<CartItemVo> getFoodItems() {
		return foodItems;
	}

	public void setFoodItems(List<CartItemVo> foodItems) {
		this.foodItems = foodItems;
	}

}

