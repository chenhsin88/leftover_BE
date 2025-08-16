package com.example.leftovers.constants;

public enum ResMessage {
	SUCCESS(200, "Success!!"),
	PASSWORD_EXISTED(400,"Password existed!!"),
	PARAM_EMAIL_ERROR(400, "Param email error!!"), //
	PARAM_PASSWORD_ERROR(400, "Param password error!!"),//
	PARAM_NEW_PASSWORD_ERROR(400, "Param new password error!!"), //
	ACCOUNT_NOT_FOUND(404, "Account not found!!"),
	NOT_FOUND(400, "Not found!!"), //
	QUANTITY_ERROR(400, "Quantity error!!"), //
	CONFLICT_ERROR(409, "Conflict error!!"),//
	DATA_NOT_FOUND(404, "Data not found!!"),
	FOOD_ITEM_MISSING(404, "Food item not found!!"),
	MERCHANT_NOT_FOUND(404, "merchant not found!!")
	//
	;
	
	private int code;

	private String message;

	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
