package com.example.leftovers.vo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class PushNotificationTokensUserReq {
	
	@Min(value = 0, message = "range 不可小於 0")
	private int range;
	
	@DecimalMin(value = "-90.0", inclusive = true, message = "緯度應介於 -90 ~ 90")
	@DecimalMax(value = "90.0", inclusive = true, message = "緯度應介於 -90 ~ 90")
	private double latitude;// 緯度

	@DecimalMin(value = "-180.0", inclusive = true, message = "經度應介於 -180 ~ 180")
	@DecimalMax(value = "180.0", inclusive = true, message = "經度應介於 -180 ~ 180")
	private double longitude;// 經度

	
	private String category;// 類別

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
