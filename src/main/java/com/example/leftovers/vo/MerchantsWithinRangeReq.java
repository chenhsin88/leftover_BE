package com.example.leftovers.vo;

public class MerchantsWithinRangeReq {

	int distance;

	double userLon;

	double userLat;

	public MerchantsWithinRangeReq() {
		super();
	}

	public MerchantsWithinRangeReq(int distance, double userLat, double userLon) {
		super();
		this.distance = distance;
		this.userLat = userLat;
		this.userLon = userLon;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
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
