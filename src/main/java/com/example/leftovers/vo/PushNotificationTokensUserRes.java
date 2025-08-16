package com.example.leftovers.vo;

import java.util.List;

public class PushNotificationTokensUserRes extends BasicRes {
	// 前台推播區畫面要抓的東西，商品圖片、商品名稱、商品描述、原價、折扣後價格、再折扣後價格、取貨時間.、商家名稱、商家地址、商家ID、食物ID
	private List<PushNotificationTokensUserVo> pushNotificationTokensUserVo;

	public PushNotificationTokensUserRes() {
		super();
	}

	public PushNotificationTokensUserRes(int code, String message) {
		super(code, message);
	}

	public PushNotificationTokensUserRes(int code, String message, List<PushNotificationTokensUserVo> pushNotificationTokensUserVo) {
		super(code, message);
		this.pushNotificationTokensUserVo = pushNotificationTokensUserVo;
	}

	public List<PushNotificationTokensUserVo> getPushNotificationTokensUserVo() {
		return pushNotificationTokensUserVo;
	}

	public void setPushNotificationTokensUserVo(List<PushNotificationTokensUserVo> pushNotificationTokensUserVo) {
		this.pushNotificationTokensUserVo = pushNotificationTokensUserVo;
	}

}
