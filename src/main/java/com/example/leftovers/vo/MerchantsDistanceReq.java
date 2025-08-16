package com.example.leftovers.vo;

public class MerchantsDistanceReq {

	int merchantId;

	double userLat;
	
	double userLon;

	public MerchantsDistanceReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public MerchantsDistanceReq(int merchantId, double userLat, double userLon) {
		super();
		this.merchantId = merchantId;
		this.userLat = userLat;
		this.userLon = userLon;
	}



	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public double getUserLat() {
		return userLat;
	}

	public void setUserLat(double userLat) {
		this.userLat = userLat;
	}

	public double getUserLon() {
		return userLon;
	}

	public void setUserLon(double userLon) {
		this.userLon = userLon;
	}

}
