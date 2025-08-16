package com.example.leftovers.vo;

import java.util.List;

import com.example.leftovers.entity.Merchants;

public class MerchantsRes extends BasicRes {

	private List<Merchants> merchants;
	
	public MerchantsRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public MerchantsRes(int code, String message, List<Merchants> merchants) {
		super( code, message);
		this.merchants = merchants;
	}

	public List<Merchants> getMerchants() {
		return merchants;
	}

	public void setMerchants(List<Merchants> merchants) {
		this.merchants = merchants;
	}

}
