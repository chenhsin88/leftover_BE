package com.example.leftovers.vo;

public class MerchantsDistanceRes extends BasicRes {

	private double distance;

	public MerchantsDistanceRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MerchantsDistanceRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public MerchantsDistanceRes(int code, String message, double distance) {
		super(code, message);
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
