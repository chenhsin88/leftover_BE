package com.example.leftovers.vo;

public class CartItemVo {
	private int foodId;
	private String foodName;
	private int price;
	private int quantity;
	private String imageUrl;
	private boolean purchased;

	// 建構子
	public CartItemVo(int foodId, String foodName, int price, int quantity, String imageUrl, boolean purchased) {
		this.foodId = foodId;
		this.foodName = foodName;
		this.price = price;
		this.quantity = quantity;
		this.imageUrl = imageUrl;
		this.purchased = purchased;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isPurchased() {
		return purchased;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}

}
