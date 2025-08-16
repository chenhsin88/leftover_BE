package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.FoodItemsDeleteQuantityReq;
import com.example.leftovers.vo.FoodItemsRes;
import com.example.leftovers.vo.FoodItemsReq;

public interface FoodItemsService {

	// 1.新增
	public BasicRes createFoodItem(FoodItemsReq req);

	// 2.更新
	public BasicRes updateFoodItem(FoodItemsReq req);

	// 3.刪除商品數量
	public BasicRes decreaseFoodItemQuantity(FoodItemsDeleteQuantityReq req);

	// 4.完全刪除整筆商品資料
	public BasicRes deleteFoodItemById(FoodItemsReq req);

	// 5.將商品設為下架
	public BasicRes deactivateFoodItem(FoodItemsReq req);

	// 6.根據店家 ID 查找所有商品
	public FoodItemsRes getFoodItemsByMerchantId(FoodItemsReq req);

	// 7.取得全部
	public FoodItemsRes getAllFoodItems(FoodItemsReq req);
}
