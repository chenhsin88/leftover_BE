package com.example.leftovers.vo;

public class OrderGetMerchantsVo {

	private String name;

	private String addressText; // 地址

	private String openingHoursDescription; // 營業時間

	private String phoneNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddressText() {
		return addressText;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

	public String getOpeningHoursDescription() {
		return openingHoursDescription;
	}

	public void setOpeningHoursDescription(String openingHoursDescription) {
		this.openingHoursDescription = openingHoursDescription;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
