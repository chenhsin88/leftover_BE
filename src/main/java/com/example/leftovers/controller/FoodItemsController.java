package com.example.leftovers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leftovers.service.ifs.FoodItemsService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.FoodItemsDeleteQuantityReq;
import com.example.leftovers.vo.FoodItemsReq;
import com.example.leftovers.vo.FoodItemsRes;


@RestController
@RequestMapping("/fooditems")
//@CrossOrigin(origins = "http://localhost:8080")
public class FoodItemsController {

	@Autowired
	private FoodItemsService foodItemsService;

	/**
	 * 1. 建立商品資料 <br>
	 * 範例:<br>
	 * {<br>
	 * "name": "草莓奶酪",<br>
	 * "description": "甜甜草莓風味", <br>
	 * "imageUrl": "/img/strawberry.jpg",<br>
	 * "originalPrice": 120,<br>
	 * "discountedPrice": 100,<br>
	 * "quantityAvailable": 8,<br>
	 * "pickupStartTime": "2025-06-07T15:00:00",<br>
	 * "pickupEndTime": "2025-06-07T18:00:00",<br>
	 * "category": "甜點",<br>
	 * "active": true <br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/create")
	public BasicRes createFoodItem(@RequestBody FoodItemsReq req) {
		return foodItemsService.createFoodItem(req);
	}

	/**
	 * 2. 更新商品資料<br>
	 * 範例:<br>
	 * {<br>
	 * "id": 1,<br>
	 * "merchantsId": 2,<br>
	 * "name": "抹茶起司蛋糕",<br>
	 * "description": "升級版抹茶口味",<br>
	 * "imageUrl": "http://example.com/matcha.jpg",<br>
	 * "originalPrice": 150,<br>
	 * "discountedPrice": 120,<br>
	 * "quantityAvailable": 8,<br>
	 * "pickupStartTime": "2025-06-08T14:00:00",<br>
	 * "pickupEndTime": "2025-06-08T17:00:00",<br>
	 * "category": "甜點",<br>
	 * "active": false<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/update")
	public BasicRes updateFoodItem(@RequestBody FoodItemsReq req) {
		return foodItemsService.updateFoodItem(req);
	}

	/**
	 * 3.刪除商品數量 <br>
	 * 例如: <br>
	 * {<br>
	 * "id": 2,<br>
	 * "deleteQuantityReq":2<br>
	 * } <br>
	 * or <br>
	 * {<br>
	 * "id": 2<br>
	 * }<br>
	 * <br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/decreaseQuantityById")
	public BasicRes decreaseFoodItemQuantity(@RequestBody FoodItemsDeleteQuantityReq req) {
		return foodItemsService.decreaseFoodItemQuantity(req);
	}

	/**
	 * 4.刪除整筆商品資料<br>
	 * {<br>
	 * "id":1,<br>
	 * "merchantsId":0<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/deleteById")
	public BasicRes deleteFoodItemById(@RequestBody FoodItemsReq req) {
		return foodItemsService.deleteFoodItemById(req);
	}

	/**
	 * 5.下架商品<br>
	 * 範例:<br>
	 * {<br>
	 * "id":2,<br>
	 * "merchantsId":0<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/deactivateById")
	public BasicRes deactivateFoodItem(@RequestBody FoodItemsReq req) {
		return foodItemsService.deactivateFoodItem(req);
	}

	/**
	 * 6. 根據店家 ID 查找所有商品<br>
	 * 範例:<br>
	 * {<br>
	 * "merchantsId":0<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/getAllByMerchantId")
	public FoodItemsRes getFoodItemsByMerchantId(@RequestBody FoodItemsReq req) {
		return foodItemsService.getFoodItemsByMerchantId(req);
	}

	/**
	 * 7.取得全部資料<br>
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping("/getAll")
	public FoodItemsRes getAllFoodItems(@RequestBody FoodItemsReq req) {
		return foodItemsService.getAllFoodItems(req);
	}
	
	// 8.商品找店家
}
