package com.example.leftovers.vo;

import java.util.List;

import com.example.leftovers.entity.FoodItems;
import com.example.leftovers.entity.Merchants;

public class MerchantWithFoodsVo extends Merchants {

	private List<FoodItems> foodList;

	public List<FoodItems> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<FoodItems> foodList) {
		this.foodList = foodList;
	}

	public MerchantWithFoodsVo() {
		super();
	}

	public MerchantWithFoodsVo(List<FoodItems> foodList) {
		super();
		this.foodList = foodList;
	}

}
