package com.example.leftovers.vo;

import java.util.List;

public class CartGetDataVo {

	private List<CartByMerchantVo> merchants;

	public CartGetDataVo() {
		super();
	}

	public CartGetDataVo(List<CartByMerchantVo> merchants) {
		super();
		this.merchants = merchants;
	}

	public List<CartByMerchantVo> getMerchants() {
		return merchants;
	}

	public void setMerchants(List<CartByMerchantVo> merchants) {
		this.merchants = merchants;
	}

	

}
