package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.CartDataRes;
import com.example.leftovers.vo.CartUpdateRes;
import com.example.leftovers.vo.CartsCreateReq;
import com.example.leftovers.vo.CartsDelteReq;
import com.example.leftovers.vo.CartsUpdateReq;

public interface CartsService {

	//將資料加入購物車
	public BasicRes createCartData(CartsCreateReq req);
	
	//更新資料
	public CartUpdateRes updateCart(CartsUpdateReq req);
	
	//刪除資料
	public BasicRes deleteCartData(CartsDelteReq req);
	
	//顯示資料
	public CartDataRes getCartByUserEmail(String email);

}
