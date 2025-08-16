package com.example.leftovers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leftovers.service.ifs.CartsService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.CartDataRes;
import com.example.leftovers.vo.CartUpdateRes;
import com.example.leftovers.vo.CartsCreateReq;
import com.example.leftovers.vo.CartsDelteReq;
import com.example.leftovers.vo.CartsUpdateReq;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/carts")
public class CartsController {

	@Autowired
	private CartsService cartService;

	// 利用買家信箱查詢買家的購物車內容
	@GetMapping("/getDataByUserEmail/{email}")
	public CartDataRes getCartByUserEmail(@PathVariable("email") String email) {
		return cartService.getCartByUserEmail(email);
	}

	/**
	 * 更新購物車商品數量 <br>
	 * 範例:<br>
	 * {<br>
	 * "userEmail": "user1@mail.com",<br>
	 * "foodItemId": 102,<br>
	 * "quantity": 5<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/update")
	public CartUpdateRes updateQuantity(@Valid @RequestBody CartsUpdateReq req) {
		return cartService.updateCart(req);
	}

	/**
	 * 刪除已經加入購物車的選項<br>
	 * * 範例:<br>
	 * {<br>
	 * "userEmail": "user1@mail.com",<br>
	 * "foodItemId": 102,<br>
	 * }<br>
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/delete")
	public BasicRes deleteCartData(@Valid @RequestBody CartsDelteReq req) {
		return cartService.deleteCartData(req);
	}
	
	@PostMapping(value = "/create")
	public BasicRes createCartData(@Valid @RequestBody CartsCreateReq req) {
		return cartService.createCartData(req);
	}
}
