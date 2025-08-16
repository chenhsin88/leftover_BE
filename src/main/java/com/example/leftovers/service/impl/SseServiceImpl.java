package com.example.leftovers.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.leftovers.service.ifs.SseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseServiceImpl implements SseService {
	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	private final Map<Integer, List<SseEmitter>> merchantEmitters = new ConcurrentHashMap<>();


	private final ObjectMapper objectMapper;
		
	public SseServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
	

	@Override
	public SseEmitter createEmitter() {
		SseEmitter emitter = new SseEmitter(0L); // 無限時間
		emitters.add(emitter);

		emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(throwable -> emitters.remove(emitter));


		return emitter;
	}




	//商家
	@Override
	public SseEmitter createMerchantEmitter(int merchantId) {
		SseEmitter emitter = new SseEmitter(0L);

		merchantEmitters.computeIfAbsent(merchantId, k -> new CopyOnWriteArrayList<>()).add(emitter);

		emitter.onCompletion(() -> merchantEmitters.get(merchantId).remove(emitter));
		emitter.onTimeout(() -> merchantEmitters.get(merchantId).remove(emitter));
		emitter.onError(e -> merchantEmitters.get(merchantId).remove(emitter));

		return emitter;
	}

	@Override
	public void pushMessageToMerchant(int merchantId, Object message) {
		List<SseEmitter> emitters = merchantEmitters.getOrDefault(merchantId, List.of());
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().name("new-order").data(message));
			} catch (IOException e) {
				emitter.completeWithError(e);
			}
		}
	}

	 @Override
	    public void pushMessage(Object data) {
	        String jsonData;
	        try {
	            // 將傳入的任意物件轉換為 JSON 字串
	            jsonData = objectMapper.writeValueAsString(data);
	        } catch (IOException e) {
	            // Log the error and return, as we can't send a corrupted message
	            System.err.println("Error serializing object to JSON: " + e.getMessage());
	            return;
	        }

	        for (SseEmitter emitter : emitters) {
	            try {
	                // 發送完整的 JSON 資料
	                emitter.send(SseEmitter.event().data(jsonData));
	            } catch (IOException e) {
	                // 當連線中斷時，Spring 會拋出 IOException
	                // 這是一個預期內的行為，我們只需要將其從列表中移除即可
	                emitters.remove(emitter);
	            }
	        }
	    }



}