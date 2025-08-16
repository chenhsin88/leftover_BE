package com.example.leftovers.vo;

public class CartUpdateRes extends BasicRes {

	private int quantity;

	public CartUpdateRes() {
		super();
	}

	public CartUpdateRes(int code, String message) {
		super(code, message);
	}

	public CartUpdateRes(int code, String message, int quantity) {
		super(code, message);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
