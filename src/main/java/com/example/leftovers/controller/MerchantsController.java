package com.example.leftovers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.leftovers.service.ifs.MerchantsService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.MerchantsAndFoodItemsRes;
import com.example.leftovers.vo.MerchantsDistanceReq;
import com.example.leftovers.vo.MerchantsDistanceRes;
import com.example.leftovers.vo.MerchantsReq;
import com.example.leftovers.vo.MerchantsRes;
import com.example.leftovers.vo.MerchantsUpdateReq;
import com.example.leftovers.vo.MerchantsWithinRangeReq;
import com.example.leftovers.vo.MerchantsDetailRes; 
import jakarta.validation.Valid;

@RestController
public class MerchantsController {

	@Autowired
	private MerchantsService merchantsService;

	/**
	 * 範例:<br>
	 * {<br>
	 * "name": "測試商家",<br>
	 * "description": "這是一家測試用的商店",<br>
	 * "addressText": "台北市中正區信義路一段100號",<br>
	 * "phoneNumber": "0912345678",<br>
	 * "contactEmail": "testmerchant@example.com",<br>
	 * "createdByEmail": "admin@example.com",<br>
	 * "logoUrl": "https://example.com/logo.png",<br>
	 * "bannerImageUrl": "https://example.com/banner.jpg",<br>
	 * "openingHoursDescription": "週一至週五 09:00 - 18:00",<br>
	 * "mapScreenshotUrl": "https://example.com/map.png",<br>
	 * "mapGoogleUrl": "https://maps.google.com/?q=Taipei",<br>
	 * "pickupInstructions": "請至門口右側櫃檯取貨"<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/merchants/register")
	public BasicRes Register(@Valid @RequestBody MerchantsReq req) throws Exception {
		return merchantsService.register(req);
	}

	/**
	 * 範例:<br>
	 * {<br>
	 * "merchantsId": 1,<br>
	 * "name": "更新後的店家名稱",<br>
	 * "description": "更新後的店家描述",<br>
	 * "addressText": "更新地址",<br>
	 * "phoneNumber": "0988123456",<br>
	 * "contactEmail": "update@example.com",<br>
	 * "logoUrl": "https://example.com/logo_updated.png",<br>
	 * "bannerImageUrl": "https://example.com/banner_updated.png",<br>
	 * "openingHoursDescription": "10:00 - 20:00",<br>
	 * "mapScreenshotUrl": "https://example.com/map_updated.png",<br>
	 * "mapGoogleUrl": "https://maps.google.com/update" <br>
	 * "pickupInstructions": "請至門口右側櫃檯取貨"<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/merchants/update")
	public BasicRes updateMerchants(@Valid @RequestBody MerchantsUpdateReq req) throws Exception {
		return merchantsService.updateMerchants(req);
	}

	/**
	 * 範例:<br>
	 * {<br>
	 * "createdByEmail": "admin@example.com"<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/merchants/getMerchantsData")
	public BasicRes getMerchantsByEmail(@Valid @RequestBody MerchantsUpdateReq req) throws Exception {
		return merchantsService.getAllMerchantsByEmail(req);
	}

	/**
	 * 用 MerchantId 取得店家所有資料<br>
	 * postman使用範例:
	 * http://localhost:8080/merchants/getMerchantsData/{MerchantId}<br>
	 * 範例:<br>
	 * http://localhost:8080/merchants/getMerchantsData/1<br>
	 * 
	 * @param merchantId
	 * @return
	 */
	@GetMapping(value = "/merchants/getMerchantsData/{merchantId}")
	public BasicRes getAllMerchantsByMerchantId(@PathVariable("merchantId") int merchantId) {
		return merchantsService.getAllMerchantsByMerchantId(merchantId);
	}

	@GetMapping(value = "/merchants/all")
	public BasicRes getAllMerchants() {
		return merchantsService.getAllMerchants();
	}

	/**
	 * (好像有問題先不要用)
	 * 查詢與該店家的距離(公里)<br>
	 * 範例:<br>
	 * {<br>
	 * "merchantId": 15,<br>
	 * "userLat": 22.6605624,<br>
	 * "userLon": 120.3195475<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/merchants/getMerchantsDistance")
	public MerchantsDistanceRes getSstoreDistance(@Valid @RequestBody MerchantsDistanceReq req) {
		return merchantsService.getSstoreDistance(req);
	}

	/**
	 * 查詢該範圍內所有店家的資訊(公里)<br>
	 * 範例:<br>
	 * {<br>
	 * "distance": 15,<br>
	 * "userLat": 22.6605624,<br>
	 * "userLon": 120.3195475<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/merchants/getMerchantsWithinRange")
	public MerchantsRes getMerchantsWithinRange(@Valid @RequestBody MerchantsWithinRangeReq req) {
		return merchantsService.getMerchantsWithinRange(req);
	}
	
	/**
	 * 查詢該範圍內所有店家與商品的資訊(公里)<br>
	 * 範例:<br>
	 * {<br>
	 * "distance": 15,<br>
	 * "userLat": 22.6605624,<br>
	 * "userLon": 120.3195475<br>
	 * }<br>
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/merchants/getMerchantAndFoodItemsWithinRange")
	public MerchantsAndFoodItemsRes getMerchantsAndFoodWithinRange(@Valid @RequestBody MerchantsWithinRangeReq req) {
		return merchantsService.getMerchantsAndFoodWithinRange(req);
	}
	
	/**
	 * 【新增的 API 端點】
	 * 專門用來取得包含評分和評論的單一商家完整資料包含資料平均星級跟評論
	 * 範例: http://localhost:8080/merchants/detail/1
	 * @param merchantId
	 * @return
	 */
	@GetMapping(value = "/merchants/detail/{merchantId}")
	public MerchantsDetailRes getMerchantDetailsWithReviews(@PathVariable("merchantId") int merchantId) {
		return merchantsService.getMerchantDetailsWithReviews(merchantId);
	}
	
	/**
	 * 【AI專用】查詢範圍內所有店家與商品資料，但【不包含】商品圖片 URL。
	 * 用於將乾淨的純文字資料提供給 AI 模型，可節省 token 並避免處理圖片 URL。
	 * 範例:<br>
	 * {<br>
	 * "distance": 15,<br>
	 * "userLat": 22.6605624,<br>
	 * "userLon": 120.3195475<br>
	 * }<br>
	 * @param req
	 * @return 回傳的資料中，所有 foodList 裡的 foodItem 的 imageUrl 會是 null。
	 */
	@PostMapping(value = "/merchants/getLightweightMerchantsWithinRange")
	public MerchantsAndFoodItemsRes getLightweightMerchantsWithinRange(@Valid @RequestBody MerchantsWithinRangeReq req) {
		return merchantsService.getLightweightMerchantsWithinRange(req);
	}
}
