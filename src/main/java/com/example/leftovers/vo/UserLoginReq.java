package com.example.leftovers.vo;

import com.example.leftovers.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserLoginReq {

	@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$", message = "信箱格式不正確")
	@NotBlank(message = ConstantsMessage.PARAM_EMAIL_ERROR)
	private String email;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,16}$", //
			message = "密碼長度必須為6~16字元，且包含大小寫英文及數字")
//	@NotBlank(message = ConstantsMessage.PARAM_PASSWORDHASH_ERROR)
	private String passwordHash;
	
//	@NotNull(message = ConstantsMessage.MISSING_LOGIN_METHOD)
	private boolean regularRegistration;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean isRegularRegistration() {
		return regularRegistration;
	}

	public void setRegularRegistration(boolean regularRegistration) {
		this.regularRegistration = regularRegistration;
	}
	
	
}
