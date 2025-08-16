package com.example.leftovers.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.leftovers.constants.ResMessage;
import com.example.leftovers.dao.FoodItemsDao;
import com.example.leftovers.dao.PushNotificationTokensDao;
import com.example.leftovers.entity.FoodItems;
import com.example.leftovers.service.ifs.FoodItemsService;
import com.example.leftovers.service.ifs.SseService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.FoodItemsDeleteQuantityReq;
import com.example.leftovers.vo.FoodItemsRes;
import com.example.leftovers.vo.FoodItemsReq;
import com.example.leftovers.vo.FoodItemsVo;

@Service
public class FoodItemsServiceImpl implements FoodItemsService {

	@Autowired
	private FoodItemsDao foodItemsDao;
	@Autowired
	private PushNotificationTokensDao pushNotificationTokensDao;
	@Autowired
	private SseService sseService;

	// 1.新增
	@Override
	public BasicRes createFoodItem(FoodItemsReq req) {
		// 1. 檢查(這裡可以加欄位驗證，略)
		// 2. 建立 foodItems 並設定欄位
		FoodItems foodItems = new FoodItems();
		foodItems.setMerchantsId(req.getMerchantsId());
		foodItems.setName(req.getName());
		foodItems.setDescription(req.getDescription());
		foodItems.setImageUrl(req.getImageUrl());
		foodItems.setOriginalPrice(req.getOriginalPrice());
		foodItems.setDiscountedPrice(req.getDiscountedPrice());
		foodItems.setQuantityAvailable(req.getQuantityAvailable());
		foodItems.setPickupStartTime(req.getPickupStartTime());
		foodItems.setPickupEndTime(req.getPickupEndTime());
		foodItems.setCategory(req.getCategory());
		foodItems.setActive(req.isActive());
		// 由後端設定時間
		foodItems.setCreatedAt(LocalDateTime.now());
		foodItems.setUpdatedAt(LocalDateTime.now());
		// 3. 存入資料庫
		foodItemsDao.save(foodItems);
		// 4. 回傳成功
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 2.更新
	@Override
	public BasicRes updateFoodItem(FoodItemsReq req) {
		int foodId = req.getId();

		// 1. 根據 ID 查詢商品資料
		Optional<FoodItems> optional = foodItemsDao.findById(foodId);
		if (optional.isEmpty()) {
			return new BasicRes(404, "找不到該商品資料");
		}

		FoodItems food = optional.get();

		// 2. 更新欄位
		food.setName(req.getName());
		food.setDescription(req.getDescription());
		food.setImageUrl(req.getImageUrl());
		food.setOriginalPrice(req.getOriginalPrice());
		food.setDiscountedPrice(req.getDiscountedPrice());
		food.setQuantityAvailable(req.getQuantityAvailable());
		food.setPickupStartTime(req.getPickupStartTime());
		food.setPickupEndTime(req.getPickupEndTime());
		food.setCategory(req.getCategory());
		food.setActive(true);// 預設只要更新就會上架
		food.setUpdatedAt(LocalDateTime.now()); // 更新時間設為現在

		// 3. 存回資料庫
		foodItemsDao.save(food);

		return new BasicRes(200, "更新成功");
	}

	// 3.刪除該商店的一筆商品數量
	@Override
	public BasicRes decreaseFoodItemQuantity(FoodItemsDeleteQuantityReq req) {
		int deleteQuantity = req.getDeleteQuantityReq();
		if (deleteQuantity < 1) {
			return new BasicRes(400, "Ddelete uantity error!!");
		}

//		int merchantsId = req.getMerchantsId();
		int foodId = req.getId();

		Optional<FoodItems> optionalFood = foodItemsDao.findById(foodId);
		if (optionalFood.isEmpty()) {
			return new BasicRes(404, "找不到該筆商品資料");
		}

		FoodItems food = optionalFood.get();

		// 這邊可能會擋到測試假資料
//		if (food.getMerchantsId() != merchantsId) {
//			return new BasicRes(403, "無權限修改此商品");
//		}

		int quantity = food.getQuantityAvailable();
		if (quantity <= 0) {
			return new BasicRes(400, "商品庫存已為 0，無法再減少");
		}

		int updatedQuantity = quantity - deleteQuantity;
		if (updatedQuantity < 0) {
			return new BasicRes(400, "刪除數量超過庫存數量");
		}

		food.setQuantityAvailable(updatedQuantity);

		// 自動下架邏輯
		if (updatedQuantity == 0) {
			food.setActive(false); // 將 is_active 設為 false（下架）
			deactivateMessage(food.getId());
		}

		food.setQuantityAvailable(quantity - req.getDeleteQuantityReq());
		food.setUpdatedAt(LocalDateTime.now());
		foodItemsDao.save(food);

		return new BasicRes(200, "商品數量已更新為：" + (quantity - req.getDeleteQuantityReq()));
	}

	// 4.刪除該商品整筆資料
	@Override
	public BasicRes deleteFoodItemById(FoodItemsReq req) {
		int merchantsId = req.getMerchantsId();
		int foodId = req.getId();

		Optional<FoodItems> optionalFood = foodItemsDao.findById(foodId);
		if (optionalFood.isEmpty()) {
			return new BasicRes(404, "找不到該筆商品資料");
		}

		FoodItems food = optionalFood.get();

		// 確保該食物屬於此店家
		if (food.getMerchantsId() != merchantsId) {
			return new BasicRes(403, "無權限刪除此商品");
		}

		foodItemsDao.deleteById(foodId);

		return new BasicRes(200, "刪除成功");
	}

	// 5.將商品設為下架
	@Override
	public BasicRes deactivateFoodItem(FoodItemsReq req) {
		// 內建查詢抓取商品唯一值ID
		Optional<FoodItems> optionalFood = foodItemsDao.findById(req.getId());
		if (optionalFood.isEmpty()) {
			return new BasicRes(404, "找不到該食物");
		}
		FoodItems food = optionalFood.get();

		// 確認該食物是屬於該店家
		if (food.getMerchantsId() != req.getMerchantsId()) {
			return new BasicRes(403, "無權限操作此店家商品");
		}

		deactivateMessage(food.getId());

		// 設定為下架、更新時間與儲存資料
		food.setActive(false);
		food.setUpdatedAt(LocalDateTime.now());
		pushNotificationTokensDao.toggle(food.getId(), false);
		foodItemsDao.save(food);

		return new BasicRes(200, "商品已下架");
	}

	// 6.根據店家 ID 查找所有商品
	@Override

	public FoodItemsRes getFoodItemsByMerchantId(FoodItemsReq req) {
		int merchantId = req.getMerchantsId();

		List<FoodItems> foodItemsList = foodItemsDao.findByMerchantsId(merchantId);

		List<FoodItemsVo> vos = foodItemsList.stream().map(item -> {
			// 判斷是否已過期，過期就設為不啟用
			if (item.getPickupEndTime().isBefore(LocalDateTime.now())) {
				item.setActive(false); // ← 將原始 Entity 標記為已下架
			}
			FoodItemsVo vo = new FoodItemsVo();
			vo.setId(item.getId());
			vo.setName(item.getName());
			vo.setDescription(item.getDescription());
			vo.setImageUrl(item.getImageUrl());
			vo.setOriginalPrice(item.getOriginalPrice());
			vo.setDiscountedPrice(item.getDiscountedPrice());
			vo.setQuantityAvailable(item.getQuantityAvailable());
			vo.setPickupStartTime(item.getPickupStartTime());
			vo.setPickupEndTime(item.getPickupEndTime());
			vo.setCategory(item.getCategory());
			vo.setActive(item.isActive());
			vo.setCreatedAt(item.getCreatedAt());
			vo.setUpdatedAt(item.getUpdatedAt());
			vo.setMerchantsId(item.getMerchantsId());// 雖然用不到這行但還是先一下之後可能改req
			return vo;
		}).toList();

		return new FoodItemsRes(200, "查詢成功", vos);
	}

	// 7.取得全部
	@Override
	public FoodItemsRes getAllFoodItems(FoodItemsReq req) {

		// 1. 從資料庫取得所有 FoodItems（可依條件過濾，例如商家ID）
		List<FoodItems> foodItemsList = foodItemsDao.findAll();

		// 2. 將 FoodItems Entity 轉成 FoodItemsVo（通常用stream map）
		List<FoodItemsVo> vos = foodItemsList.stream()
				.filter(entity -> entity.isActive() && entity.getPickupEndTime().isAfter(LocalDateTime.now()))
				.map(entity -> {
					FoodItemsVo vo = new FoodItemsVo();
					vo.setId(entity.getId());
					vo.setMerchantsId(entity.getMerchantsId());
					vo.setName(entity.getName());
					vo.setDescription(entity.getDescription());
					vo.setImageUrl(entity.getImageUrl());
					vo.setOriginalPrice(entity.getOriginalPrice());
					vo.setDiscountedPrice(entity.getDiscountedPrice());
					vo.setQuantityAvailable(entity.getQuantityAvailable());
					vo.setPickupStartTime(entity.getPickupStartTime());
					vo.setPickupEndTime(entity.getPickupEndTime());
					vo.setCategory(entity.getCategory());
					vo.setActive(entity.isActive());
					vo.setCreatedAt(entity.getCreatedAt());
					vo.setUpdatedAt(entity.getUpdatedAt());
					return vo;
				}).collect(Collectors.toList());
		// 3. 包裝成回傳物件
		return new FoodItemsRes(200, "查詢成功", vos);
	}

	// SSE推播下架通知
	private void deactivateMessage(int foodId) {
		System.out.println("強制下架的商品: " + foodId);

		// 建立 JSON 格式字串，包含 type 與 foodId 兩個欄位
		String jsonMessage = String.format("{\"type\":\"DEACTIVATE\",\"foodId\":%d,\"message\":\"強制下架的商品: %d\"}",
				foodId, foodId);

		// 傳送 JSON 訊息給 SSE
//	    sseService.pushMessage(jsonMessage);
	}

	// 食物過期自動下架
	public void deactivateExpiredFoodItems() {
		List<FoodItems> allItems = foodItemsDao.findAll(); // 撈全部商品
		List<FoodItems> expiredItems = new ArrayList<>();
		for (FoodItems item : allItems) {
			// 如果商品是上架中且已經過了取餐結束時間，就改為下架
			if (item.isActive() && item.getPickupEndTime().isBefore(LocalDateTime.now())) {
				item.setActive(false);
				item.setUpdatedAt(LocalDateTime.now());
				expiredItems.add(item); // 收集要更新的項目
			}
		}
		// 批次儲存
		if (!expiredItems.isEmpty()) {
			foodItemsDao.saveAll(expiredItems);

			// 建立要推播的 JSON 陣列格式
//			String jsonMessage = buildDeactivationBatchMessage(expiredItems);
//			sseService.pushMessage(jsonMessage);
			System.out.println("自動下架商品數量：" + expiredItems.size());
		}
	}

	// JSON 封裝方法
	private String buildDeactivationBatchMessage(List<FoodItems> items) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"type\":\"BATCH_FOOD_DEACTIVATE\",\"items\":[");

		for (int i = 0; i < items.size(); i++) {
			FoodItems item = items.get(i);
			sb.append(String.format("{\"foodId\":%d,\"message\":\"商品 '%s' 已自動下架（超過取餐時間）\"}", item.getId(),
					item.getName()));

			if (i < items.size() - 1) {
				sb.append(",");
			}
		}

		sb.append("]}");
		return sb.toString();
	}
}
