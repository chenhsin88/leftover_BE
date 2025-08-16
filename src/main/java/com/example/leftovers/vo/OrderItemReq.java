package com.example.leftovers.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderItemReq {
	
		@NotNull(message = "商家ID不能空")
		private int merchantsId;
		
		@NotNull(message = "訂單ID不能空")
		private int orderId;
		
	 	@NotNull(message = "食物ID不能空")
	    private int foodId;

	    @NotNull(message = "食物數量不能空")
	    private int quantity;
	    
	    @NotBlank(message = "食物名稱不能空")
	    private String foodName;
	    
	    @NotNull(message = "食物價錢不能空")
	    private int foodPrice;
	    
	    private String merchant;
	    
	 

		

		public int getMerchantsId() {
			return merchantsId;
		}

		public void setMerchantsId(int merchantsId) {
			this.merchantsId = merchantsId;
		}

		public int getOrderId() {
			return orderId;
		}

		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}

		public int getFoodId() {
			return foodId;
		}

		public void setFoodId(int foodId) {
			this.foodId = foodId;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public String getFoodName() {
			return foodName;
		}

		public void setFoodName(String foodName) {
			this.foodName = foodName;
		}

		public int getFoodPrice() {
			return foodPrice;
		}

		public void setFoodPrice(int foodPrice) {
			this.foodPrice = foodPrice;
		}

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}
	    
	   
	    
}
