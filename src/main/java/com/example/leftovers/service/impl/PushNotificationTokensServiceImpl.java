package com.example.leftovers.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.leftovers.constants.ResMessage;
import com.example.leftovers.dao.FoodItemsDao;
import com.example.leftovers.dao.MerchantsDao;
import com.example.leftovers.dao.PushNotificationTokensDao;
import com.example.leftovers.entity.FoodItems;
import com.example.leftovers.entity.Merchants;
import com.example.leftovers.entity.PushNotificationTokens;
import com.example.leftovers.service.ifs.PushNotificationTokensService;
import com.example.leftovers.service.ifs.SseService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.PushNotificationTokensDto;
import com.example.leftovers.vo.PushNotificationTokensReq;
import com.example.leftovers.vo.PushNotificationTokensRes;
import com.example.leftovers.vo.PushNotificationTokensUserDto;
import com.example.leftovers.vo.PushNotificationTokensUserReq;
import com.example.leftovers.vo.PushNotificationTokensUserRes;
import com.example.leftovers.vo.PushNotificationTokensUserVo;

@Service
public class PushNotificationTokensServiceImpl implements PushNotificationTokensService {

	@Autowired
	private PushNotificationTokensDao pushNotificationTokensDao;
	@Autowired
	private MerchantsDao merchantsDao;
	@Autowired
	private FoodItemsDao foodItemsDao;
	@Autowired
	private FoodItemsServiceImpl foodItemsService;
	@Autowired
	private SseService sseService;
	/**
	 * 新增一個全域變數，用來儲存上一次成功推播的資料 (foodItemId -> price) 這個 Map 會在多次排程執行之間保持狀態。
	 */
	private Map<Integer, Integer> lastPushedData = new HashMap<>();

	/**
	 * 推播畫面需要的資料 (還有問題需要解決 價格的顯示 時間到下架) <br>
	 * 1.判斷有沒有此商家id <br>
	 * 2.食物如果沒了就會自動下架 <br>
	 * 3.保留設定後的價錢 <br>
	 * 4.保留設定後的時間轉換為幾小時 <br>
	 */
	@Override
	public PushNotificationTokensRes UIData(PushNotificationTokensReq req) {
		// 自動建立可推播的食物資料
		pushNotificationTokensDao.getAndCreateData();

		int merchantsId = req.getMerchantsId();

		boolean exists = merchantsDao.existsById(merchantsId);
		if (!exists) {
			return new PushNotificationTokensRes(ResMessage.DATA_NOT_FOUND.getCode(),
					ResMessage.DATA_NOT_FOUND.getMessage());
		}
		List<PushNotificationTokensDto> res = pushNotificationTokensDao.UIData(merchantsId);
		// 把 newPrice = 0 的設成 null
		for (PushNotificationTokensDto dto : res) {
			if (dto.getNewPrice() != null && dto.getNewPrice() == 0) {
				dto.setNewPrice(null);
			}
		}
		// 回傳封裝結果
		return new PushNotificationTokensRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), res);
	}

	/**
	 * 是否啟用推播的功能 <br>
	 * 
	 * 再次折扣後的價格 <br>
	 * 1.判斷商品是否存在 <br>
	 * 2.判斷價格合理性 <br>
	 * 3.設定的時間到價格才可以再折扣(未完成) <br>
	 * 
	 * 設定時間 <br>
	 * 1.輸入的幾小時候->轉成幾點後更換 <br>
	 * 2.判斷時間合理性
	 */
	@Override
	public BasicRes isRepeatDiscountEnabled(PushNotificationTokensReq req) {
		int foodItemId = req.getFoodItemsId();
		int newPrice = req.getNewPrice();
		int afterHours = req.getDefaultHours();
//		LocalDateTime time = req.getSetTime();
		boolean isActive = req.isPushNotifications();
		int discountedPrice = pushNotificationTokensDao.findPreviousPriceByFoodItemId(foodItemId);
		// 修改價格
		Optional<FoodItems> optionalFood = foodItemsDao.findById(foodItemId);

		if (!optionalFood.isPresent()) {
			return new BasicRes(400, "商品不存在");
		}
		if (newPrice <= 0) {
			return new BasicRes(400, "價格必須大於0");
		}

		if (newPrice > discountedPrice) {
			return new BasicRes(400, "折扣價格不能高於原價");
		}
		// 設定時間
		LocalDateTime setTime = null;

		LocalDateTime now = LocalDateTime.now(); // 取得現在時間
		setTime = pushNotificationTokensDao.getPickupEndTime(foodItemId);
		setTime = setTime.minusHours(afterHours); // 減去 afterHours 小時
		// 檢查現在時間是否早於觸發時間（也就是無法再設定）
		if (now.isAfter(setTime)) {
			System.out.println("我有擋住");
			return new BasicRes(400, "時間已過，無法再設定推播");
		}

		// 若沒開啟推播，不儲存觸發時間
		if (!isActive) {
			pushNotificationTokensDao.setTime(foodItemId, afterHours, null);
			System.out.println("修改原本價格discountedPrice: " + discountedPrice + "新價格newPrice: " + newPrice);
			pushNotificationTokensDao.setPrice(discountedPrice, foodItemId);
			pushNotificationTokensDao.toggle(foodItemId, isActive);
			return new BasicRes(200, "推播開關尚未開啟，設定參數不會保存");
		}

		// 修改價格
//		pushNotificationTokensDao.setPrice(newPrice, foodItemId);
		pushNotificationTokensDao.newPrice(newPrice, foodItemId);
		// 偵測時間是否到了再自動更改價格
		autoUpdateDiscountedPrices();
		// 設定時間進資料庫
		pushNotificationTokensDao.setTime(foodItemId, afterHours, setTime);
		System.err.println(setTime + "的時候開始推播");
		// 切換開關
		pushNotificationTokensDao.toggle(foodItemId, isActive);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	/**
	 * 偵測時間修改價格
	 */
	@Scheduled(fixedRate = 6000) // 每 6 秒執行一次
	public void autoUpdateDiscountedPrices() {

		Map<Integer, Integer> currentPushData = new HashMap<>();
		LocalDateTime now = LocalDateTime.now();
		List<PushNotificationTokens> tokensToUpdate = pushNotificationTokensDao.findTokensToUpdate(now);

		for (PushNotificationTokens token : tokensToUpdate) {
			// 推播截止時間
			LocalDateTime endTime = token.getSetTime().plusHours(token.getDefaultHours());
			// 推播時間到會自行關閉
			if (now.isAfter(endTime)) {
				pushNotificationTokensDao.toggle(token.getFoodItemsId(), false);
			}
			int newPrice = token.getNewPrice();
			int foodItemId = token.getFoodItemsId();
			currentPushData.put(foodItemId, newPrice);
			pushNotificationTokensDao.setPrice(newPrice, foodItemId);
		}

		Set<Integer> currentItemIds = currentPushData.keySet();
		Set<Integer> lastItemIds = this.lastPushedData.keySet();

		Set<Integer> newItemIds = new HashSet<>(currentItemIds);
		newItemIds.removeAll(lastItemIds);

		if (!newItemIds.isEmpty()) {
			System.out.println("偵測到新商品加入折扣，準備推播：" + newItemIds);

			// 根據新的商品ID，查詢完整的資料
			List<FoodItems> newFoodItems = foodItemsDao.findAllById(newItemIds);

			// 取得這些商品對應的商家ID
			List<Integer> merchantIds = newFoodItems.stream().map(FoodItems::getMerchantsId).distinct()
					.collect(Collectors.toList());

			// 一次性查詢所有相關的商家資料
			Map<Integer, Merchants> merchantMap = merchantsDao.findAllById(merchantIds).stream()
					.collect(Collectors.toMap(Merchants::getMerchantsId, m -> m));

			// 將完整的商品和商家資訊組裝成 VO
			List<PushNotificationTokensUserVo> newItemsVoList = newFoodItems.stream().map(food -> {
				Merchants merchant = merchantMap.get(food.getMerchantsId());
				if (merchant == null) {
					return null; // 如果找不到對應的商家，則跳過
				}

				// 這裡的距離無法針對每個使用者計算，所以設為0或null，讓前端自己處理或忽略
				// 如果需要顯示距離，前端在收到推播後，可以拿商家座標和自己的座標進行計算
				int distance = 0;

				String pickupTimeStr = "今天 " + now.format(DateTimeFormatter.ofPattern("HH:mm")) + " ~ "
						+ food.getPickupEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));

				return new PushNotificationTokensUserVo(merchant.getMerchantsId(), food.getId(), merchant.getName(),
						merchant.getAddressText(), distance, // 距離設為0
						food.getCategory(), food.getName(), food.getDescription(), food.getImageUrl(),
						food.getOriginalPrice(), food.getDiscountedPrice(), currentPushData.get(food.getId()), // 使用當前折扣的最新價格
						pickupTimeStr);
			}).filter(vo -> vo != null).collect(Collectors.toList());

			if (!newItemsVoList.isEmpty()) {
				// 直接將組裝好的 List<VO> 物件推播出去
				// SseServiceImpl 會自動將其轉換為 JSON 格式
				sseService.pushMessage(newItemsVoList);
//				System.out.println("已成功推送 " + newItemsVoList.size() + " 筆新折扣商品的完整資料。");
			}
		}

		this.lastPushedData = currentPushData;
	}


	// 每 15 分鐘執行一次 偵測商品是否過期 
//	@Scheduled(cron = "0 0/15 * * * ?")
	public void autoDeactivateExpiredItems() {
		foodItemsService.deactivateExpiredFoodItems();
	}

	/**
	 * 1.抓取推播表內有開啟食物推播的店家ID 2.過濾掉距離太遠的店家 3.抓取所有推播食物資訊、店家資訊 時間到跟推播開
	 */
	@Override
	public PushNotificationTokensUserRes userUIData(PushNotificationTokensUserReq req) {

		List<PushNotificationTokensUserVo> voList = new ArrayList<>();

		// Dto抓取資料 再過濾用VO回傳需要的資料
		List<PushNotificationTokensUserDto> dtoList = pushNotificationTokensDao.getUserUi();

		// 轉換時間格式 -> yyyy-MM-dd HH:mm:ss 轉為 HH:mm
//		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime now = LocalDateTime.now();
		for (PushNotificationTokensUserDto dto : dtoList) {
			// 檢查座標，若沒有座標無法知道距離
			if (dto.getLongitudeAndLatitude() == null) {
				continue;
			}

			String[] lonAndLat = dto.getLongitudeAndLatitude().split(",");

			try {
				double lon = Double.parseDouble(lonAndLat[0].trim());
				double lat = Double.parseDouble(lonAndLat[1].trim());

				int distance = (int) haversine(lat, lon, req.getLatitude(), req.getLongitude());

				// 限制距離: 過濾買家設定的距離
				if (distance > req.getRange()) {
					continue;
				}

				// 假設 dto.getSetTime() 是 String，要先轉成 LocalDateTime
				LocalDateTime setTime = dto.getSetTime();
				LocalDateTime pickupTime = dto.getPickupTime();

				if (setTime == null || pickupTime == null) {
					continue;
				}
				// 如果現在時間還沒到 setTime，跳過這筆
				if (now.isBefore(setTime)) {
					continue;
				}

				// 塞選喜歡的食物類別，沒有相符的類別就過濾
				if (req.getCategory() != null && !req.getCategory().equals(dto.getCategory())) {
					continue;
				}

				// 將時間封裝為 '今天 HH:mm ~ HH:mm' 回傳
				String pickupTimeStr = "今天 " + setTime.format(timeFormatter) + " ~ " + pickupTime.format(timeFormatter);

				PushNotificationTokensUserVo vo = new PushNotificationTokensUserVo(dto.getMerchantsId(), // 商家 ID
						dto.getFoodItemsId(), // 食物 ID
						dto.getMerchantName(), // 商家名稱
						dto.getAddressText(), // 商家地址
						distance, // 距離
						dto.getCategory(), // 類別
						dto.getFoodName(), // 食物名稱
						dto.getDescription(), // 食物描述
						dto.getImageUrl(), // 食物圖片
						(int) dto.getOriginalPrice(), // 原價
						(int) dto.getDiscountedPrice(), // 折扣價
						(int) dto.getNewPrice(), // 最終價格
						pickupTimeStr // 取貨時間
				);
				voList.add(vo);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		if (voList.isEmpty()) {
			return new PushNotificationTokensUserRes(ResMessage.FOOD_ITEM_MISSING.getCode(),
					ResMessage.FOOD_ITEM_MISSING.getMessage());
		}
		return new PushNotificationTokensUserRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), voList); // 之後填入內容
	}

	// 計算距離公式 傳入兩點的經緯度（單位：度）
	private double haversine(double lat1, double lon1, double lat2, double lon2) {
		double EARTH_RADIUS = 6371.0; // 地球半徑，單位：公里
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double rLat1 = Math.toRadians(lat1);
		double rLat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(rLat1) * Math.cos(rLat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c;
	}

}
