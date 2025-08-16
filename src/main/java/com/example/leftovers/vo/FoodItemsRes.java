package com.example.leftovers.vo;

import java.util.List;

public class FoodItemsRes extends BasicRes {

	private List<FoodItemsVo> vos;

	public FoodItemsRes() {
		super();
	}

	public FoodItemsRes(int code, String message) {
		super(code, message);
	}

	public FoodItemsRes(int code, String message, List<FoodItemsVo> vos) {
		super(code, message);
		this.vos = vos;
	}

	// 加上 getter 和 setter 很重要！！
	public List<FoodItemsVo> getVos() {
		return vos;
	}

	public void setVos(List<FoodItemsVo> vos) {
		this.vos = vos;
	}
}