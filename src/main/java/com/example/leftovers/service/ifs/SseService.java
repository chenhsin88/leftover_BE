package com.example.leftovers.service.ifs;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

	public SseEmitter createEmitter();

	public SseEmitter createMerchantEmitter(int merchantId);

	public void pushMessageToMerchant(int merchantId, Object message);

	/**
	 * 推播一個物件給所有前端，該物件會被自動序列化為 JSON
	 * 
	 * @param data 要推播的資料物件
	 */
	public void pushMessage(Object data);

}
