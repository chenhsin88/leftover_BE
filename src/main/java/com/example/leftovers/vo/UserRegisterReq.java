package com.example.leftovers.vo;

import com.example.leftovers.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserRegisterReq {


	@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$", message = "信箱格式不正確")
	@NotBlank(message = ConstantsMessage.PARAM_EMAIL_ERROR)
	private String email;
	
//	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,16}$", //
//			message = "密碼長度必須為6~16字元，且包含大小寫英文及數字")
//	@NotNull(message = ConstantsMessage.PARAM_PASSWORDHASH_ERROR)
	private String passwordHash;
	
	@NotBlank(message = ConstantsMessage.PARAM_NAME_ERROR)
	private String name;
	
	@Pattern(regexp = "^09\\d{8}$", message = "手機號碼格式錯誤，應為09開頭共10位數字")
	@NotBlank(message = ConstantsMessage.PARAM_PHONE_NUMBER_ERROR)
	private String phoneNumber;
	
	private String  profilePictureUrl;
	
	@NotBlank(message = ConstantsMessage.PARAM_ROLE_ERROR)
	private String role;
	
	@NotNull(message = ConstantsMessage.PARAM_IS_ACTIVE_ERROR)
	private boolean isActive;
	
	@NotNull(message = ConstantsMessage.MISSING_LOGIN_METHOD)
	private boolean RegularRegistration;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isRegularRegistration() {
		return RegularRegistration;
	}

	public void setRegularRegistration(boolean regularRegistration) {
		RegularRegistration = regularRegistration;
	}
	
	
}
