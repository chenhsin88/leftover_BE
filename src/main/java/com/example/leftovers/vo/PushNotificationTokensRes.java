package com.example.leftovers.vo;

import java.util.List;

public class PushNotificationTokensRes extends BasicRes {
	
	private List<PushNotificationTokensDto> dto;

	public PushNotificationTokensRes() {
		super();
	}

	public PushNotificationTokensRes(int code, String message) {
		super(code, message);
	}

	public PushNotificationTokensRes(int code, String message, List<PushNotificationTokensDto> dto) {
		super(code, message);
		this.dto = dto;
	}

	public List<PushNotificationTokensDto> getDto() {
		return dto;
	}

	public void setDto(List<PushNotificationTokensDto> dto) {
		this.dto = dto;
	}

	
	
}
