package com.example.leftovers.vo;

public class CartDataRes extends BasicRes {

	private CartGetDataVo cartDataVo;

	public CartGetDataVo getCartDataVo() {
		return cartDataVo;
	}

	public void setCartDataVo(CartGetDataVo cartDataVo) {
		this.cartDataVo = cartDataVo;
	}

	public CartDataRes() {
		super();
	}

	public CartDataRes(int code, String message) {
		super(code, message);
	}

	public CartDataRes(int code, String message, CartGetDataVo cartDataVo) {
		super(code, message);
		this.cartDataVo = cartDataVo;
	}

}
