package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.PushNotificationTokensReq;
import com.example.leftovers.vo.PushNotificationTokensRes;
import com.example.leftovers.vo.PushNotificationTokensUserReq;
import com.example.leftovers.vo.PushNotificationTokensUserRes;

public interface PushNotificationTokensService {

	//商家畫面資料
	public PushNotificationTokensRes UIData(PushNotificationTokensReq req);
	
	//推播通知開關
	public BasicRes isRepeatDiscountEnabled(PushNotificationTokensReq req);
	
	//使用者接收推播資訊
	public PushNotificationTokensUserRes userUIData(PushNotificationTokensUserReq req);
	

}
