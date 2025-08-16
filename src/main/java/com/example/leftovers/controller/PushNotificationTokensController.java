package com.example.leftovers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leftovers.service.ifs.PushNotificationTokensService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.PushNotificationTokensReq;
import com.example.leftovers.vo.PushNotificationTokensRes;
import com.example.leftovers.vo.PushNotificationTokensUserReq;
import com.example.leftovers.vo.PushNotificationTokensUserRes;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pushNotificationTokens")
//@CrossOrigin(origins = "http://localhost:8080")
public class PushNotificationTokensController {

	@Autowired
	private PushNotificationTokensService pushNotificationTokensService;

	/**
	 * 板面資料 <br>
	 * 範例: <br>
	 * { <br>
	 * "merchantsId": 1 <br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/getUI")
	public PushNotificationTokensRes getUIData(@Valid @RequestBody PushNotificationTokensReq req) throws Exception {
		return pushNotificationTokensService.UIData(req);
	}

	/**
	 * 推播開關<br>
	 * 範例:<br>
	 * {<br>
	 * "foodItemsId": 3,<br>
	 * "newPrice": 50,<br>
	 * "defaultHours": 2,<br>
	 * "pushNotifications": true<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/toggle")
	public BasicRes isRepeatDiscountEnabled(@Valid @RequestBody PushNotificationTokensReq req) throws Exception {
		return pushNotificationTokensService.isRepeatDiscountEnabled(req);
	}

	/**
	 * 使用者取得推播資料
	 * <p>
	 * 此方法依據使用者的目前位置與條件，過濾符合條件的推播資料（例如距離與食物類別），
	 * 並回傳附近可取貨的優惠食物資訊。
	 * </p>
	 * Postman 測試API: <br>
	 * http://localhost:8080/pushNotificationTokens/getUserUI <br>
	 * <p>請求範例：</p>
	 * "category":用來塞選商品類別 可以不用輸入<br>
	 * <pre>
	 * {
	 *   "latitude": 22.45857,          // 使用者目前緯度
	 *   "longitude": 120.23858,        // 使用者目前經度
	 *   "range": 3000,                 // 搜尋範圍（單位：公里）
	 *   "category": null               // 食物分類（可為 null 表示全部）
	 * }
	 * </pre>
	 * @param req 推播請求參數物件 {@link PushNotificationTokensUserReq}
	 * @return 包含推播顯示用資料的結果 {@link PushNotificationTokensUserRes}
	 */

	@PostMapping("/getUserUI")
	public PushNotificationTokensUserRes getUserUIData(@Valid @RequestBody PushNotificationTokensUserReq req) {
		return pushNotificationTokensService.userUIData(req);
	}

}
