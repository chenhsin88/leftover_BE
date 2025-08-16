package com.example.leftovers.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.leftovers.constants.ResMessage;
import com.example.leftovers.dao.CartsDao;
import com.example.leftovers.dao.FoodItemsDao;
import com.example.leftovers.dao.MerchantsDao;
import com.example.leftovers.entity.Carts;
import com.example.leftovers.entity.FoodItems;
import com.example.leftovers.entity.Merchants;

import com.example.leftovers.service.ifs.CartsService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.CartByMerchantVo;
import com.example.leftovers.vo.CartDataRes;
import com.example.leftovers.vo.CartGetDataVo;
import com.example.leftovers.vo.CartItemVo;
import com.example.leftovers.vo.CartUpdateRes;
import com.example.leftovers.vo.CartsCreateReq;
import com.example.leftovers.vo.CartsDelteReq;
import com.example.leftovers.vo.CartsUpdateReq;

import jakarta.transaction.Transactional;

@Service
public class CartsServiceImpl implements CartsService {
	@Autowired
	private CartsDao cartsDao;

	@Autowired
	private FoodItemsDao foodItemsDao;

	@Autowired
	private MerchantsDao merchantsDao;
	
	@Autowired 
	private CartsDao cartsdao;
	
	
	//每天中午12點刪除一次
	@Scheduled(cron = "0 0 12 * * ?")
	@Transactional
	public void cleanCarts() {
	    try {
	        LocalDateTime deadline = LocalDateTime.now().minusDays(1);
	        List<Carts> carts = cartsdao.findByCreatedAtBefore(deadline);

	        if (!carts.isEmpty()) {
	            cartsdao.deleteAll(carts);
//	            System.out.println("已清除 " + carts.size() + " 筆購物車資料");
	        } else {
//	            System.out.println("無需清除購物車資料");
	        }
	    } catch (Exception e) {
	        System.err.println("清除購物車時發生錯誤: " + e.getMessage());
	    }
	}
	
	/**
	 * 顯示購物車的資料
	 *  1.商品名稱 
	 *  2.商品價格 
	 *  3.商品圖片 
	 *  4.商店名稱
	 */
	@Override
	public CartDataRes getCartByUserEmail(String email) {
		// 查詢該使用者 email 所有購物車資料
		List<Carts> cartList = cartsDao.findByUserEmail(email);
		// 建立一個 Map 用來依據商家 ID 分類購物車內容
		Map<Integer, CartByMerchantVo> merchantMap = new HashMap<>();

		// 每一筆購物車資料
		for (Carts cart : cartList) {
			// 查詢此購物車項目的食物與商家資訊
			FoodItems food = foodItemsDao.findById(cart.getFoodItemId()).orElse(null);
			Merchants merchant = merchantsDao.findById(cart.getMerchantsId()).orElse(null);
			// 若查不到商品或商家資料，則跳過這筆紀錄
			if (food == null || merchant == null) {
				continue;
			}
			
//			isPurchased 感覺也可以不用
			// 判斷此商品是否為「已購買」，若狀態為 "purchased"，則為 true
			// ⚠️ 建議你這裡改成 "converted_to_order"（因為資料庫定義是這樣）
			boolean isPurchased = "converted_to_order".equalsIgnoreCase(cart.getStatus());

			// 將此購物車商品封裝為 VO 物件，便於回傳使用
			CartItemVo itemVo = new CartItemVo(//
					food.getId(), //
					food.getName(), //
					food.getDiscountedPrice(), //
					cart.getQuantity(), //
					food.getImageUrl(), //
					isPurchased//
			);//

			// 根據商家 ID 取得目前的 VO，若無則初始化一筆新的商家資料
			CartByMerchantVo merchantVo = merchantMap.get(cart.getMerchantsId());
			if (merchantVo == null) {
				merchantVo = new CartByMerchantVo();
				merchantVo.setMerchantId(cart.getMerchantsId());
				merchantVo.setMerchantName(merchant.getName());
				merchantVo.setFoodItems(new ArrayList<>());
				merchantMap.put(cart.getMerchantsId(), merchantVo);
			}
			// 將該商品加入商家的商品清單
			merchantVo.getFoodItems().add(itemVo);

		}

		CartGetDataVo cartGetDataVo = new CartGetDataVo(new ArrayList<>(merchantMap.values()));
		return new CartDataRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), //
				cartGetDataVo);
	}

	@Override
	public BasicRes createCartData(CartsCreateReq req) {
		//檢查商品是否存在
		LocalDateTime now = LocalDateTime.now();
		Optional<FoodItems> foodItemOpt = foodItemsDao.findById(req.getFoodItemId());
		if (foodItemOpt.isEmpty()) {
		    return new BasicRes(ResMessage.FOOD_ITEM_MISSING.getCode(), "商品不存在");
		}		
		Optional<Merchants> merchantOpt = merchantsDao.findById(req.getMerchantId());
		if (merchantOpt.isEmpty()) {
		    return new BasicRes(ResMessage.MERCHANT_NOT_FOUND.getCode(), "商家不存在");
		}
		if (req.getQuantity() <= 0) {
		    return new BasicRes(ResMessage.QUANTITY_ERROR.getCode(), "數量必須大於 0");
		}	
		
		 FoodItems foodItem = foodItemOpt.get();
		    int availableQuantity = foodItem.getQuantityAvailable();
		  // 檢查是否已有相同商品在購物車（相同使用者 + 商家 + 商品 + active 狀態）
	    Optional<Carts> existingCartOpt = cartsDao.findByUserEmailAndMerchantsIdAndFoodItemIdAndStatus(
	            req.getUserEmail(), req.getMerchantId(), req.getFoodItemId(), "active");

	    if (existingCartOpt.isPresent()) {
	        // 若已存在，更新數量
	        Carts existingCart = existingCartOpt.get();
	        int newTotalQuantity = existingCart.getQuantity() + req.getQuantity();
	        if (newTotalQuantity > availableQuantity) {
	            return new BasicRes(ResMessage.QUANTITY_ERROR.getCode(), "庫存不足，無法加入更多。目前庫存: " + availableQuantity);
	        }
	        existingCart.setQuantity(existingCart.getQuantity() + req.getQuantity());
	        cartsDao.save(existingCart);
	    } else {
	    	if (req.getQuantity() > availableQuantity) {
	            return new BasicRes(ResMessage.QUANTITY_ERROR.getCode(), "庫存不足。目前庫存: " + availableQuantity);
	        }
	        // 若不存在，新增一筆
	        Carts carts = new Carts();
	        carts.setUserEmail(req.getUserEmail());
	        carts.setMerchantsId(req.getMerchantId());
	        carts.setCreatedAt(now);
	        carts.setQuantity(req.getQuantity());
	        carts.setFoodItemId(req.getFoodItemId());
	        carts.setStatus("active");
	        cartsDao.save(carts);
	    }

	    return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	/**
	 * 更新購物車商品的數量
	 */
	@Override
	public CartUpdateRes updateCart(CartsUpdateReq req) {
		LocalDateTime now = LocalDateTime.now();
		// 1. 先查詢是否已有該使用者、食物與商家的購物車紀錄
		List<Carts> cartsList = cartsDao.findAllByUserEmailAndFoodItemId(req.getUserEmail(), req.getFoodItemId());
		if (cartsList.isEmpty()) {
			return new CartUpdateRes(ResMessage.DATA_NOT_FOUND.getCode(), ResMessage.DATA_NOT_FOUND.getMessage());
		}
		// 檢查商品是否下架
		boolean activate = foodItemsDao.findIsActiveById(req.getFoodItemId());
		if (!activate) {
			return new CartUpdateRes(ResMessage.FOOD_ITEM_MISSING.getCode(), ResMessage.FOOD_ITEM_MISSING.getMessage());
		}
		// 檢查商品數量
		int quantity = foodItemsDao.findQuantityAvailableById(req.getFoodItemId());
		if (req.getQuantity() < 1) {
			return new CartUpdateRes(ResMessage.QUANTITY_ERROR.getCode(), ResMessage.QUANTITY_ERROR.getMessage());
		}
		if (req.getQuantity() > quantity) {
			return new CartUpdateRes(ResMessage.QUANTITY_ERROR.getCode(),
					ResMessage.QUANTITY_ERROR.getMessage() + "商品數量不可大於:" + quantity, quantity);
		}

		// ✔ 這裡就可以拿來用，前提是你保證資料只會有一筆
		Carts carts = cartsList.get(0);
		// 狀態更改感覺也不用
//		carts.setStatus(req.getStatus());
		carts.setQuantity(req.getQuantity());
		// 時間感覺不用
		carts.setCreatedAt(now);
//		carts.setUpdatedAt(req.getUpdatedAt());
		cartsDao.save(carts);
		return new CartUpdateRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	/**
	 * 刪除加入購物車裡面的商品
	 */
	@Override
	public BasicRes deleteCartData(CartsDelteReq req) {
		List<Carts> cartsList = cartsDao.findAllByUserEmailAndFoodItemId(req.getUserEmail(), req.getFoodItemId());
		if (cartsList.isEmpty()) {
			return new BasicRes(ResMessage.DATA_NOT_FOUND.getCode(), ResMessage.DATA_NOT_FOUND.getMessage());
		}
		// ✅ 刪除全部
		cartsDao.deleteAll(cartsList);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

}
