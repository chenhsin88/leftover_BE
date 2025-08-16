package com.example.leftovers.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import com.example.leftovers.constants.ResMessage;
import com.example.leftovers.dao.FoodItemsDao;
import com.example.leftovers.dao.HistoryOrderDao;
import com.example.leftovers.dao.MerchantsDao;
import com.example.leftovers.entity.FoodItems;
import com.example.leftovers.entity.HistoryOrder;
import com.example.leftovers.entity.Merchants;
import com.example.leftovers.service.ifs.MerchantsService;
import com.example.leftovers.service.ifs.NominatimService;
import com.example.leftovers.service.impl.NominatimServiceImpl.AddressUtils;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.MerchantWithFoodsVo;
import com.example.leftovers.vo.MerchantsAndFoodItemsRes;
import com.example.leftovers.vo.MerchantsDistanceReq;
import com.example.leftovers.vo.MerchantsDistanceRes;
import com.example.leftovers.vo.MerchantsReq;
import com.example.leftovers.vo.MerchantsRes;
import com.example.leftovers.vo.MerchantsUpdateReq;
import com.example.leftovers.vo.MerchantsWithinRangeReq;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.leftovers.vo.ReviewVo;
import com.example.leftovers.dao.ReviewsDao;
import com.example.leftovers.dao.UserDao;
import com.example.leftovers.entity.Reviews;
import com.example.leftovers.entity.User;
import com.example.leftovers.exception.ResourceNotFoundException;
import com.example.leftovers.service.ifs.ReviewsService;
import com.example.leftovers.vo.MerchantsDetailRes;
import com.example.leftovers.vo.ReviewsGetByMerchantsRes;
import com.example.leftovers.dao.FoodItemsDao; // 確保引入 FoodItemsDao
import com.example.leftovers.entity.FoodItems;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class MerchantsServiceImpl implements MerchantsService {
	@Autowired
	private MerchantsDao merchantsDao;

	@Autowired
	private FoodItemsDao foodItemsDao;

	@Autowired
	private ReviewsDao reviewsDao;

	@Autowired
	private ReviewsService reviewsService;

	@Autowired
	private NominatimService nominatimService;

	@Autowired
	private HistoryOrderDao historyOrderDao; // <-- Spring 注入了這個物件

	@Autowired
	private UserDao userDao;
	// OpenStreetMap
//	private final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

	@Override
	public BasicRes register(MerchantsReq req) {
		String longitude_and_latitude = null;

		// 1. 基本欄位驗證 RegisterMerchantsReq 內部已處理
		// 2. 檢查是否已註冊過 尚未檢查
		int check = merchantsDao.checkMerchantsName(req.getName());
		if (check == 1) {
			return new MerchantsRes(409, "店名已註冊過，請使用其他名稱");
		}
		// 3. 設定註冊時的預設欄位
		req.setActive(true);// 預設驗證通過
		req.setApprovedByAdminId(0); // 或改為 null（若欄位允許）
		req.setApprovedAt(null);// 初始註冊為 null
		// 4.取得商家座標位置
		// 地址正規化
		String normalizedAddress = AddressUtils.normalize(req.getAddressText());

		try {
			double[] coords = nominatimService.getCoordinatesFromAddress(normalizedAddress);
			System.out.println("查詢結果經緯度: 經度=" + coords[0] + ", 緯度=" + coords[1]);
			longitude_and_latitude = coords[0] + "," + coords[1];
		} catch (Exception e) {
			return new BasicRes(400, "地址無法轉換成經緯度，請重新確認地址格式");
		}

		// 4. 存入資料庫
		merchantsDao.register(req.getName(), req.getDescription(), //
				req.getAddressText(), //
				req.getPhoneNumber(), //
				req.getContactEmail(), //
				req.getCreatedByEmail(), //
				req.getLogoUrl(), //
				req.getBannerImageUrl(), //
				req.getOpeningHoursDescription(), //
				req.getMapScreenshotUrl(), //
				req.getMapGoogleUrl(), //
				req.isActive(), //
				req.getApprovedByAdminId(), //
				req.getApprovedAt(), //
				req.getPickupInstructions(), //
				longitude_and_latitude // 經緯度的座標
		);
		// 5. 回傳成功
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override // 未設定正規表達
	public BasicRes updateMerchants(MerchantsUpdateReq req) {
		// 1. 從安全上下文中取得當前登入者的資訊
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentLoggedInUserEmail = authentication.getName(); // 取得 Email

	    // 2. 取得要被修改的商店的原始資料
	    List<Merchants> originalMerchantList = merchantsDao.getAllMerchantsByMerchantsId(req.getMerchantsId());
	    if (originalMerchantList.isEmpty()) {
	        throw new ResourceNotFoundException("找不到 ID 為 " + req.getMerchantsId() + " 的商家"); // 使用自訂例外
	    }
	    Merchants originalMerchant = originalMerchantList.get(0);

	    // 3. ★★★ 核心安全檢查：比對 소유권 (所有權) ★★★
	    // 確認當前登入的使用者，是否就是這家店的建立者
	    if (!originalMerchant.getCreatedByEmail().equals(currentLoggedInUserEmail)) {
	        // 如果不是，拋出 AccessDeniedException，Spring Security 會捕捉到它
	        throw new AccessDeniedException("權限不足：你不能修改不屬於你的商店。");
	    }
	    
		String longitude_and_latitude = null;
		// 檢查 id 是否存在（必要）
		int checkId = merchantsDao.findByMerchantsId(req.getMerchantsId());
		if (checkId != 1) {
			return new BasicRes(ResMessage.DATA_NOT_FOUND.getCode(), ResMessage.DATA_NOT_FOUND.getMessage());
		}

		// 如果使用者填的新名稱 ≠ 原本這間商家的名稱
		int check = merchantsDao.checkMerchantsName(req.getName());
		if (!req.getName().equals(merchantsDao.findNameByMerchantsId(req.getMerchantsId()))) {
			if (check == 1) {
				return new MerchantsRes(409, "店名已註冊過，請使用其他名稱");
			}
		}
		// 地址正規化
		String normalizedAddress = AddressUtils.normalize(req.getAddressText());
		try {
			double[] coords = nominatimService.getCoordinatesFromAddress(normalizedAddress);
			System.out.println("查詢結果經緯度: 經度=" + coords[0] + ", 緯度=" + coords[1]);
			longitude_and_latitude = coords[0] + "," + coords[1];
		} catch (Exception e) {
			return new BasicRes(400, "地址無法轉換成經緯度，請重新確認地址格式");
		}
		// 執行更新
		// 設定註冊時的預設欄位 要改再說,地址更新還沒寫
		req.setActive(true);// 預設驗證通過
		req.setApprovedByAdminId(0); // 或改為 null（若欄位允許）
		req.setApprovedAt(null);// 初始註冊為 null

		merchantsDao.updateMerchant(//
				req.getName(), //
				req.getDescription(), //
				req.getAddressText(), //
				req.getPhoneNumber(), //
				req.getContactEmail(), //
				req.getLogoUrl(), //
				req.getBannerImageUrl(), //
				req.getOpeningHoursDescription(), //
				req.getMapScreenshotUrl(), //
				req.getMapGoogleUrl(), //
				req.isActive(), //
				req.getApprovedByAdminId(), //
				req.getApprovedAt(), //
				req.getPickupInstructions(), //
				longitude_and_latitude,//
				req.getMerchantsId());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public MerchantsRes getAllMerchantsByEmail(MerchantsUpdateReq req) {
		// 檢查 Email 是否存在（必要）
		int checkEmail = merchantsDao.getByEmail(req.getCreatedByEmail());
		if (checkEmail < 1) {
			return new MerchantsRes(ResMessage.DATA_NOT_FOUND.getCode(), ResMessage.DATA_NOT_FOUND.getMessage());
		}

		List<Merchants> res = merchantsDao.getAllMerchantsByEmail(req.getCreatedByEmail()); // 取得所有資料

		return new MerchantsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), res);
	}

	@Override
	public MerchantsRes getAllMerchantsByMerchantId(int MerchantsId) {
		// 檢查 Email 是否存在（必要）

//		if(checkEmail < 1) {
//			return new MerchantsRes(ResMessage.DATA_NOT_FOUND.getCode(),ResMessage.DATA_NOT_FOUND.getMessage());
//		}

		List<Merchants> res = merchantsDao.getAllMerchantsByMerchantsId(MerchantsId); // 取得所有資料

		if (res == null || res.isEmpty()) {
			return new MerchantsRes(ResMessage.DATA_NOT_FOUND.getCode(), "查無該商家 ID 對應的資料");
		}

		return new MerchantsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), res);
	}

	// 前台取得所有使用者資訊
	@Override
	public MerchantsRes getAllMerchants() {
		List<Merchants> merchantsList = merchantsDao.findAll();
		if (merchantsList.isEmpty()) {
			return new MerchantsRes(404, "查無商家資料");
		}
		return new MerchantsRes(200, "查詢成功", merchantsList);
	}

	// 計算與該店家的距離
	@Override
	public MerchantsDistanceRes getSstoreDistance(MerchantsDistanceReq req) {
		Optional<String> storeLongitudeAndLatitude = merchantsDao
				.getLongitudeAndLatitudeByMerchantsId(req.getMerchantId());
		int checkId = merchantsDao.findByMerchantsId(req.getMerchantId());
		if (checkId != 1) {
			return new MerchantsDistanceRes(ResMessage.DATA_NOT_FOUND.getCode(),
					ResMessage.DATA_NOT_FOUND.getMessage());
		}
		if (storeLongitudeAndLatitude.isEmpty()) {
			System.out.println("查無商家的經緯度資料！");
			return new MerchantsDistanceRes(ResMessage.DATA_NOT_FOUND.getCode(),
					ResMessage.DATA_NOT_FOUND.getMessage()); // 或丟出例外、給預設距離
		}
		System.out.println("storeLongitudeAndLatitude 物件：" + storeLongitudeAndLatitude.get());
		// 切割字串成 [緯度, 經度]
		String[] parts = storeLongitudeAndLatitude.get().split(",");

		double storeLon = Double.parseDouble(parts[0].trim()); // 經度
		double storeLat = Double.parseDouble(parts[1].trim()); // 緯度

		System.out.println("經度 Longitude:" + storeLon + " 緯度 Latitude:" + storeLat);
		// 計算距離
		double distance = haversine(storeLat, storeLon, req.getUserLat(), req.getUserLon());
		System.out.printf("距離為：%.2f 公里%n", distance);

		// 回傳實際距離
		return new MerchantsDistanceRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), distance);
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

	// 取得範圍內的所有店家資訊
	@Override
	public MerchantsRes getMerchantsWithinRange(MerchantsWithinRangeReq req) {

		List<Merchants> merchantsList = merchantsDao.findAll();

		List<Merchants> filteredMerchants = new ArrayList<>();
		if (merchantsList.isEmpty()) {
			return new MerchantsRes(404, "查無商家資料");
		}

		for (Merchants merchant : merchantsList) {
			String longLatStr = merchant.getLongitudeAndLatitude();

			if (longLatStr == null || longLatStr.isBlank()) {
				// 可加上 log 或跳過這筆
//				System.out.println("商家 " + merchant.getMerchantsId() + " 的經緯度為 null，已跳過");
				continue;
			}

			// 切割字串成 [緯度, 經度]
			String[] parts = merchant.getLongitudeAndLatitude().split(",");
			double storeLon = Double.parseDouble(parts[0].trim());
			double storeLat = Double.parseDouble(parts[1].trim());
			// 計算距離
			double distance = haversine(storeLat, storeLon, req.getUserLat(), req.getUserLon());

			if (distance <= req.getDistance()) {
				filteredMerchants.add(merchant);
			}
		}
		// 查看回傳資料是不是空的
		if (filteredMerchants.isEmpty()) {
			return new MerchantsRes(ResMessage.MERCHANT_NOT_FOUND.getCode(),
					ResMessage.MERCHANT_NOT_FOUND.getMessage());
		}
		return new MerchantsRes(200, "查詢成功", filteredMerchants);
	}

	// 取得範圍內所有店家與食物資料
	@Override
	public MerchantsAndFoodItemsRes getMerchantsAndFoodWithinRange(MerchantsWithinRangeReq req) {

//		List<Merchants> merchantsList = merchantsDao.findAll();
//		List<FoodItems> foodItemsList = foodItemsDao.findAll();
//		
//		List<Merchants> filteredMerchants = new ArrayList<>();
//		
//		for (Merchants merchant : merchantsList) {
//			String longLatStr = merchant.getLongitudeAndLatitude();
//
//			if (longLatStr == null || longLatStr.isBlank()) {
//				// 可加上 log 或跳過這筆
////				System.out.println("商家 " + merchant.getMerchantsId() + " 的經緯度為 null，已跳過");
//				continue;
//			}
//
//			// 切割字串成 [緯度, 經度]
//			String[] parts = merchant.getLongitudeAndLatitude().split(",");
//			double storeLon = Double.parseDouble(parts[0].trim());
//			double storeLat = Double.parseDouble(parts[1].trim());
//			// 計算距離
//			double distance = haversine(storeLat, storeLon, req.getUserLat(), req.getUserLon());
//
//			if (distance <= req.getDistance()) {
//				filteredMerchants.add(merchant);
//			}
//		}
		// 1. 第一次查詢：高效取得所有在範圍內的店家
		List<Merchants> filteredMerchants = merchantsDao.findMerchantsWithinRange(req.getUserLat(), req.getUserLon(),
				req.getDistance());

		if (filteredMerchants.isEmpty()) {
			return new MerchantsAndFoodItemsRes(ResMessage.MERCHANT_NOT_FOUND.getCode(),
					ResMessage.MERCHANT_NOT_FOUND.getMessage());
		}

		// 2. 從第一次的結果中，收集所有店家的 ID
		List<Integer> merchantIds = filteredMerchants.stream().map(Merchants::getMerchantsId)
				.collect(Collectors.toList());

		// 3. 第二次查詢：一次性取得所有相關店家的所有上架商品
		List<FoodItems> allFoodItems = foodItemsDao.findByMerchantsIdIn(merchantIds).stream()
				.filter(FoodItems::isActive).collect(Collectors.toList());

		// 4. 在 Java 記憶體中，將商品按店家 ID 分組，方便後續組合
		Map<Integer, List<FoodItems>> foodItemsByMerchantIdMap = allFoodItems.stream()
				.collect(Collectors.groupingBy(FoodItems::getMerchantsId));

		// 5. 組裝最終的 Vo 物件
		List<MerchantWithFoodsVo> merchantVoList = filteredMerchants.stream().map(merchant -> {
			// 從 Map 中取得該店家的商品列表，如果沒有則給一個空列表
			List<FoodItems> foodList = foodItemsByMerchantIdMap.getOrDefault(merchant.getMerchantsId(),
					new ArrayList<>());

			// 如果該店家沒有任何「上架」的商品，就跳過
			if (foodList.isEmpty()) {
				return null;
			}
			MerchantWithFoodsVo merchantVo = new MerchantWithFoodsVo();

			// 3. ★★★ 將 merchant 的所有欄位完整複製到 merchantVo ★★★
			// 因為 MerchantWithFoodsVo 繼承了 Merchants，所以可以直接使用所有 setter
			merchantVo.setMerchantsId(merchant.getMerchantsId());
			merchantVo.setName(merchant.getName());
			merchantVo.setCreatedByEmail(merchant.getCreatedByEmail());
			merchantVo.setDescription(merchant.getDescription());
			merchantVo.setAddressText(merchant.getAddressText());
			merchantVo.setPhoneNumber(merchant.getPhoneNumber());
			merchantVo.setContactEmail(merchant.getContactEmail());
			merchantVo.setOpening_hoursDescription(merchant.getOpening_hoursDescription());
			merchantVo.setApprovedByAdminId(merchant.getApprovedByAdminId());
			merchantVo.setApprovedAt(merchant.getApprovedAt());
			merchantVo.setLogoUrl(merchant.getLogoUrl());
			merchantVo.setBannerImageUrl(merchant.getBannerImageUrl());
			merchantVo.setMapScreenshotUrl(merchant.getMapScreenshotUrl());
			merchantVo.setMapGoogleUrl(merchant.getMapGoogleUrl());
			merchantVo.setPickupInstructions(merchant.getPickupInstructions());
			merchantVo.setLongitudeAndLatitude(merchant.getLongitudeAndLatitude()); // << 包含我們需要的經緯度
			merchantVo.setActive(merchant.isActive());

			// 4. 設定專屬的 foodList 欄位
			merchantVo.setFoodList(foodList);

			return merchantVo;

		}).filter(Objects::nonNull).collect(Collectors.toList());

		// 查看回傳資料是不是空的
		if (merchantVoList.isEmpty()) {
			return new MerchantsAndFoodItemsRes(ResMessage.MERCHANT_NOT_FOUND.getCode(),
					ResMessage.MERCHANT_NOT_FOUND.getMessage());
		}
		// 回傳物件
		return new MerchantsAndFoodItemsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),
				merchantVoList);
	}

	@Override
	public MerchantsDetailRes getMerchantDetailsWithReviews(int merchantId) {
		// 1. 取得商家基本資料
		Optional<Merchants> merchantOpt = merchantsDao.findById(merchantId);
		if (!merchantOpt.isPresent()) {
			return new MerchantsDetailRes(404, "找不到此商家");
		}
		Merchants merchant = merchantOpt.get();

		// 2. 取得該商家的所有評論 (第 1 次 DB 查詢)
		List<Reviews> reviews = reviewsDao.findByMerchantId(merchantId);

		// 如果沒有評論，就直接回傳，無需後續查詢
		if (reviews.isEmpty()) {
			List<FoodItems> foodList = foodItemsDao.findByMerchantsIdAndIsActive(merchantId, true);
			merchant.setFoodList(foodList);

			MerchantsDetailRes res = new MerchantsDetailRes(200, "成功取得商家資料(無評論)");
			res.setMerchant(merchant);
			res.setAverageRating(0.0);
			res.setReviewCount(0);
			res.setReviews(Collections.emptyList());
			return res;
		}

		// 3. 從評論中，收集所有原始的 orderId，並轉換成 "RF" 開頭的字串格式
		List<String> historyOrderIds = reviews.stream().map(r -> "RF" + r.getOrderId()).distinct().toList();

		// 4. 一次性查詢所有相關的歷史訂單 (第 2 次 DB 查詢)
		List<HistoryOrder> historyOrders = historyOrderDao.findAllByOrderIdIn(historyOrderIds);
		// 建立一個 Map，方便用原始 orderId (數字) 快速找到 userEmail
		Map<Long, String> orderIdToEmailMap = historyOrders.stream()
				.collect(Collectors.toMap(h -> Long.parseLong(h.getOrderId().substring(2)), // 從 "RF123" 取出 123
						HistoryOrder::getUserEmail, (existing, replacement) -> existing // 如果有重複的 key，保留第一個
				));

		// 5. 從上一步的 Map 中，收集所有不重複的 userEmail
		List<String> userEmails = new ArrayList<>(new HashSet<>(orderIdToEmailMap.values()));

		// 6. 一次性查詢所有相關的使用者資料 (第 3 次 DB 查詢)
		List<User> users = userDao.findAllById(userEmails);
		// 建立一個 Map，方便用 email 快速找到頭像 URL
		Map<String, String> emailToAvatarMap = users.stream()
				.collect(Collectors.toMap(User::getEmail, User::getProfilePictureUrl));

		// 7. 組合最終的 ReviewVo 列表，並填入頭像
		List<ReviewVo> reviewVosWithAvatar = reviews.stream().map(r -> {
			ReviewVo vo = new ReviewVo();
			vo.setRating(r.getRating());
			vo.setComment(r.getComment());
			vo.setUserName(r.getUserName());
			vo.setCreatedAt(r.getCreatedAt());
			vo.setMerchantReply(r.getMerchantReply());
			vo.setMerchantReplyAt(r.getMerchantReplyAt());

			// 從 Map 中快速查找對應的頭像
			String email = orderIdToEmailMap.get(r.getOrderId());
			String avatarUrl = (email != null) ? emailToAvatarMap.get(email) : null;
			vo.setProfilePictureUrl(avatarUrl);

			return vo;
		}).toList();

		// 8. 計算平均分 (維持不變)
		double averageRating = reviewVosWithAvatar.stream().mapToInt(ReviewVo::getRating).average().orElse(0.0);

		// 9. 取得食物清單 (第 4 次 DB 查詢)
		List<FoodItems> foodList = foodItemsDao.findByMerchantsIdAndIsActive(merchantId, true);
		merchant.setFoodList(foodList);

		// 10. 組合最終的回應物件
		MerchantsDetailRes res = new MerchantsDetailRes(200, "成功取得商家詳細資料");
		res.setMerchant(merchant);
		res.setAverageRating(averageRating);
		res.setReviewCount(reviewVosWithAvatar.size());
		res.setReviews(reviewVosWithAvatar);

		return res;
	}
	
	//AI專用的查詢方法，取得範圍內所有店家與商品的資訊，但不包含商品圖片 URL
	@Override
	public MerchantsAndFoodItemsRes getLightweightMerchantsWithinRange(MerchantsWithinRangeReq req) {
	    // 1. 執行與原方法相同的資料庫查詢，取得商家和商品的完整資料
	    MerchantsAndFoodItemsRes originalResponse = getMerchantsAndFoodWithinRange(req);

	    // 如果查詢失敗或查無資料，直接回傳原始的回應
	    if (originalResponse.getCode() != 200 || originalResponse.getMerchantsWithFoods() == null) {
	        return originalResponse;
	    }

	    // 2. 【核心】在回傳前，遍歷所有資料並清除指定的 Base64 欄位
	    for (MerchantWithFoodsVo merchant : originalResponse.getMerchantsWithFoods()) {
	        
	        // 【新增的程式碼】清除商家層級的 Base64 欄位
	        merchant.setLogoUrl(null);
	        merchant.setMapGoogleUrl(null);

	        // 【原本的程式碼】清除商品層級的圖片網址
	        if (merchant.getFoodList() != null) {
	            for (FoodItems food : merchant.getFoodList()) {
	                food.setImageUrl(null); // 或者 food.setImageUrl("");
	            }
	        }
	    }

	    // 3. 回傳沒有 Base64 欄位的乾淨資料
	    return originalResponse;
	}
}
