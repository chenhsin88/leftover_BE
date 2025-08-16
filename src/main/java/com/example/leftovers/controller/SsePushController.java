package com.example.leftovers.controller;

import com.example.leftovers.service.ifs.SseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SsePushController {

	private final SseService sseService;

	public SsePushController(SseService sseService) {
		this.sseService = sseService;
	}

	@GetMapping("/sse")
	public SseEmitter connect() {
		return sseService.createEmitter();
	}

	@PostMapping("/trigger")
	public String trigger(@RequestBody String message) {
		sseService.pushMessage("通知前端：" + message);
		return "推播完成";
	}

	@PostMapping("/my-api")
	public Map<String, Object> receiveFromFront(@RequestBody Map<String, Object> payload) {
		System.out.println("前端觸發回應 API：" + payload);
		return Map.of("status", "received", "original", payload);
	}

	@GetMapping("/sse/merchant/{merchantId}")
	public SseEmitter connectMerchant(@PathVariable("merchantId") int merchantId) {
	    return sseService.createMerchantEmitter(merchantId);
	}

	@PostMapping("/notify/merchant/{merchantId}")
	public String notifyMerchant(@PathVariable("merchantId") int merchantId, @RequestBody String message) {
		sseService.pushMessageToMerchant(merchantId, message);
		return "商家通知新訂單完成";
	}

}
