package com.example.leftovers.vo;

public class UserRoleRes extends BasicRes {

	private String role;

	private boolean regularRegistration;

	public UserRoleRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRoleRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public UserRoleRes(int code, String message, String role, boolean regularRegistration) {
		super(code, message);
		this.role = role;
		this.regularRegistration = regularRegistration;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isRegularRegistration() {
		return regularRegistration;
	}

	public void setRegularRegistration(boolean regularRegistration) {
		this.regularRegistration = regularRegistration;
	}

}
