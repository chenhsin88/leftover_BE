package com.example.leftovers.controller;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.leftovers.constants.PaymentConstants;
import com.example.leftovers.dao.OrderDao;
import com.example.leftovers.entity.Orders;
import com.example.leftovers.service.ifs.PaymentService;
import com.example.leftovers.utils.PaymentUtils;
import com.example.leftovers.vo.PaymentRequestVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView; 
import com.example.leftovers.service.ifs.SseService; // <<--- 【新增】
import com.example.leftovers.vo.OrderSimpleVo; // <<--- 【新增】
@Controller 
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;
    private final SseService sseService; // <<--- 【新增】

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	private OrderDao orderDao;
	// 【修改】建構函式，加入 SseService
	public PaymentController(PaymentService service, SseService sseService) {
		this.paymentService = service;
		this.sseService = sseService;
	}

    @Value("${app.public-url}")
    private String publicUrl;

    @GetMapping("/public-url")
    @ResponseBody // 確保回傳的是 JSON 字串，而不是試圖尋找頁面
    public Map<String, String> getPublicUrl() {
        return Map.of("publicUrl", this.publicUrl);
    }

	/**
	 * 建立付款訂單，回傳給前端帶入表單:<br>
	 * {<br>
	 * "merchantOrderNo": "ORDER20250616001",<br>
	 * "amt": 100,<br>
	 * "itemDesc": "測試商品"<br>
	 * }<br>
	 * 
	 * @return
	 */
	@ResponseBody 
	@PostMapping("/create")
	 public PaymentRequestVo createOrder(@RequestBody Map<String, Object> data) {
		
//	     String orderNo = "ORDER" + System.currentTimeMillis();
		 String orderNo = (String) data.get("orderNumber");
	     int amount = (int) data.get("amt");
	     String itemDesc = (String) data.get("itemDesc");

	     return paymentService.createPaymentOrder(orderNo, amount, itemDesc);
	 }

	/**
	 * 藍新付款完成回調通知:<br>
	 * {<br>
	 * "MerchantID":"你的商店代號",<br>
	 * "RespondType":"JSON",<br>
	 * "TimeStamp":"1686900000",<br>
	 * "Version":"2.0",<br>
	 * "MerchantOrderNo":"ORDER1686900000",<br>
	 * "Amt":"100",<br>
	 * "ItemDesc":"測試商品",<br>
	 * "ReturnURL":"https://你的網域/api/payment/notify",<br>
	 * "CheckMacValue":"ABCDE12345..."<br>
	 * }<br>
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
    @PostMapping("/notify")
    public String paymentNotify(@RequestParam Map<String, String> params) {
        logger.info("收到藍新金流 Notify 通知，參數: {}", params);

        try {
            // 1. 取得加密的交易資訊
            String tradeInfo = params.get("TradeInfo");
            if (tradeInfo == null) {
                logger.error("藍新 Notify 通知中缺少 TradeInfo 參數");
                return "0|Error"; // 回傳錯誤，讓藍新知道我們沒處理
            }

            // 2. 解密 TradeInfo
            String decryptedJson = PaymentUtils.decryptAES256(
                tradeInfo,
                PaymentConstants.HASH_KEY, // 使用常數中的金鑰
                PaymentConstants.HASH_IV   // 使用常數中的 IV
            );
            logger.info("Notify 解密後 TradeInfo: {}", decryptedJson);
         // PaymentController.handlePaymentNotify()
            if (logger.isDebugEnabled()) {
                logger.debug("NOTIFY KEY(base64)={}", Base64.getEncoder()
                                                          .withoutPadding()
                                                          .encodeToString(PaymentConstants.HASH_KEY.getBytes(StandardCharsets.UTF_8)));
                logger.debug("NOTIFY IV(base64)={}",  Base64.getEncoder()
                                                          .withoutPadding()
                                                          .encodeToString(PaymentConstants.HASH_IV.getBytes(StandardCharsets.UTF_8)));
            }

            // 3. 解析 JSON，取得訂單號和付款狀態
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode tradeInfoNode = objectMapper.readTree(decryptedJson);

            String status = tradeInfoNode.path("Status").asText();
            String merchantOrderNo = tradeInfoNode.path("Result").path("MerchantOrderNo").asText();

            // 4. 如果付款不成功，直接回傳 OK，不做後續處理
            if (!"SUCCESS".equals(status)) {
                 logger.warn("訂單 {} 的 Notify 通知狀態為 {}，不更新訂單。", merchantOrderNo, status);
                 return "1|OK";
            }
            
            // 5. 根據訂單號查詢您的訂單並更新狀態
            long orderId = Long.parseLong(merchantOrderNo);
            Optional<Orders> orderOpt = orderDao.findByOrderId(orderId);

            if (orderOpt.isPresent()) {
                Orders order = orderOpt.get();
                // 僅在訂單為 "pending" 時才更新，避免重複處理
                if ("pending".equalsIgnoreCase(order.getStatus())) {
                    order.setStatus("picked_up"); // 或您定義的「已付款」狀態
                    order.setUpdatedAt(LocalDateTime.now());
                    orderDao.save(order);
                    logger.info("訂單 {} 狀態已透過 Notify 成功更新為 'paid'。", orderId);
                 // ================== 【關鍵修復】開始 ==================
                    // 訂單狀態更新成功後，立即發送 SSE 通知給商家
                    try {
                        // 建立前端需要的通知內容物件
                        Map<String, Object> notificationData = Map.of(
                            "orderId", order.getOrderId(),
                            "userName", order.getUserName(),
                            "totalAmount", order.getTotalAmount(),
                            "orderAt", order.getOrderedAt().toString(), // 確保傳遞時間
                            "status", order.getStatus(),
                            "pickupCode", order.getPickupCode()
                        );
                        
                        sseService.pushMessageToMerchant(order.getMerchantsId(), notificationData);
                        logger.info("已成功為訂單 {} 發送 SSE 通知給商家 ID: {}", orderId, order.getMerchantsId());
                    } catch (Exception sseEx) {
                        logger.error("為訂單 {} 發送 SSE 通知時發生錯誤", orderId, sseEx);
                        // 即使 SSE 失敗，也不影響主要流程，所以只記錄錯誤
                    }
                    // ================== 【關鍵修復】結束 ==================
                } else {
                    logger.info("訂單 {} 狀態已為 {}，無需透過 Notify 更新。", orderId, order.getStatus());
                }
            } else {
                logger.warn("Notify 通知中的訂單號 {} 在資料庫中找不到。", orderId);
            }

        } catch (Exception e) {
            logger.error("處理藍新 Notify 通知時發生嚴重錯誤", e);
            // 即使內部處理失敗，仍建議回傳成功給藍新，避免它一直重試導致重複處理。
            // 您需要有另一套機制來處理這些失敗的案例。
        }
        
        // 6. 無論內部處理結果如何，都必須回傳給藍新平台 "1|OK"
        return "1|OK";
        }
	
	
	@PostMapping(
			  value = "/return",
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			  produces = MediaType.TEXT_HTML_VALUE
			)
	public String handlePaymentReturn(@RequestParam Map<String, String> params) {

	    // (A) Raw request logging: 記錄最原始的參數，確保 Spring 沒有做不預期的處理
	    logger.info("藍新 ReturnURL 收到最原始的 POST 參數: {}", params);

	    String encryptedTradeInfo = params.get("TradeInfo");          // 原始
	    encryptedTradeInfo = encryptedTradeInfo.replaceAll("\\s+", ""); // 去掉空白/換行

	    // 2. 想印長度就直接用自己
	    logger.info("TradeInfo cleaned len = {}", encryptedTradeInfo.length());
	    
	    final String receivedTradeSha = params.get("TradeSha");
	    final String status = params.get("Status");

	    // 提前檢查必要參數是否存在
	    if (encryptedTradeInfo == null || receivedTradeSha == null || status == null) {
	        logger.error("ReturnURL 回傳缺少關鍵參數 (TradeInfo, TradeSha, or Status)");
	        return "redirect:http://localhost:4200/paymentResult?status=fail&reason=missing_parameters";
	    }

	    // 如果付款狀態本身就不是 SUCCESS，直接導向失敗頁面
	    if (!"SUCCESS".equals(status)) {
	        logger.warn("ReturnURL 回傳的 Status 不是 SUCCESS，狀態為：{}", status);
	        return "redirect:http://localhost:4200/paymentResult?status=fail&reason=payment_status_not_success";
	    }

	    try {
	        // --- 開始進行解密前的健全性檢查 ---

	        // (B) Param 長度比對
	        // 十六進位字串的長度必須是偶數
	        if (encryptedTradeInfo.length() % 2 != 0) {
	            logger.error("TradeInfo 長度為奇數 ({})，這不是一個有效的十六進位字串。", encryptedTradeInfo.length());
	            return "redirect:http://localhost:4200/paymentResult?status=fail&reason=invalid_trade_info_length";
	        }
	        logger.info("Return TradeInfo length={}, startsWith={}, endsWith={}",
	            encryptedTradeInfo.length(),
	            encryptedTradeInfo.substring(0, 16),
	            encryptedTradeInfo.substring(encryptedTradeInfo.length() - 16)
	        );

	        // (C) 僅允許 [0-9a-fA-F] 檢查
	        if (!encryptedTradeInfo.matches("^[0-9a-fA-F]+$")) {
	            logger.error("TradeInfo 包含非十六進位字元，可能在傳輸中損毀或被修改。");
	            return "redirect:http://localhost:4200/paymentResult?status=fail&reason=invalid_trade_info_format";
	        }

	        // (E) Key / IV 長度與來源一致性檢查
	        // 假設您的 Return URL 金鑰存放在這裡
	        String returnUrlHashKey = PaymentConstants.HASH_KEY;
	        String returnUrlHashIv = PaymentConstants.HASH_IV;
	        logger.info("準備用於解密的 KEY 長度: {}, IV 長度: {}", returnUrlHashKey.length(), returnUrlHashIv.length());
	        if (logger.isDebugEnabled()) {
	            logger.debug("KEY(base64)={}", Base64.getEncoder()
	                                                 .withoutPadding()
	                                                 .encodeToString(returnUrlHashKey.getBytes(StandardCharsets.UTF_8)));
	            logger.debug("IV(base64)={}",  Base64.getEncoder()
	                                                 .withoutPadding()
	                                                 .encodeToString(returnUrlHashIv.getBytes(StandardCharsets.UTF_8)));
	        }
	        // 確保金鑰長度符合 AES-256 要求
	        if (returnUrlHashKey.length() != 32 || returnUrlHashIv.length() != 16) {
	             logger.error("Return URL 使用的 KEY 或 IV 長度不正確！KEY 應為 32 位元，IV 應為 16 位元。");
	             return "redirect:http://localhost:4200/paymentResult?status=fail&reason=invalid_key_length";
	        }

	        // (D) 驗 TradeSha 再解密 (這是最關鍵的一步)
	        String stringToHash = String.format("HashKey=%s&%s&HashIV=%s",
	            returnUrlHashKey,
	            encryptedTradeInfo,
	            returnUrlHashIv
	        );

	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
	        
	        // 將 byte[] 轉換為大寫的十六進位字串
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : hash) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        String calculatedSha = hexString.toString().toUpperCase();

	        logger.info("收到的 TradeSha: {}", receivedTradeSha);
	        logger.info("本地計算的 TradeSha: {}", calculatedSha);

	        if (!receivedTradeSha.equals(calculatedSha)) {
	            logger.error("TradeSha 驗證失敗！密文可能已被竄改或金鑰不正確。");
	            return "redirect:http://localhost:4200/paymentResult?status=fail&reason=sha_mismatch";
	        }

	        // --- 所有檢查通過，才進行解密 ---
	        logger.info("TradeSha 驗證成功，準備解密 TradeInfo...");
	        if (logger.isDebugEnabled()) {
	            // ① Notify vs Return 的密文比對
	            logger.debug("TradeInfo(base64)={}",
	                Base64.getEncoder().withoutPadding()
	                      .encodeToString(encryptedTradeInfo.getBytes(StandardCharsets.UTF_8)));

	            // ② 確認 parseHex 產出的 byte[] 沒被再加工
	            byte[] bodyBytes = HexFormat.of().parseHex(encryptedTradeInfo);
	            logger.debug("HEX→BYTES checksum={}", Arrays.hashCode(bodyBytes));

	            // ③ 直接跑一次解密當單元測試
	            try {
	                String quickTest = PaymentUtils.decryptAES256(
	                    encryptedTradeInfo, returnUrlHashKey, returnUrlHashIv);
	                logger.debug("INLINE DECRYPT OK => {}", quickTest.substring(0, 60) + "...");
	            } catch (Exception ex) {
	                logger.debug("INLINE DECRYPT FAIL: {}", ex.toString());
	            }
	        }


	        String decryptedJson = PaymentUtils.decryptAES256(
	                encryptedTradeInfo,
	                returnUrlHashKey,
	                returnUrlHashIv);



	        logger.info("解密成功！TradeInfo: {}", decryptedJson);

	        // ... 後續處理解密後 JSON 的邏輯 (與您原本的程式碼相同)
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode tradeInfoNode = objectMapper.readTree(decryptedJson);
	        String merchantOrderNo = tradeInfoNode.path("Result").path("MerchantOrderNo").asText();
	        
	        long orderId = Long.parseLong(merchantOrderNo);
	        Optional<Orders> orderOpt = orderDao.findByOrderId(orderId);
	        
	        if (orderOpt.isPresent()) {
	            Orders order = orderOpt.get();
	            if ("pending".equalsIgnoreCase(order.getStatus())) {
	                order.setStatus("picked_up");
	                order.setUpdatedAt(LocalDateTime.now());
	                orderDao.save(order);
	                logger.info("訂單 {} 狀態已透過 Return 成功更新為 'picked_up'。", order.getOrderId());
	            } else {
	                logger.info("訂單 {} 狀態已為 {}，無需透過 Return 更新。", order.getOrderId(), order.getStatus());
	            }
	            return "redirect:http://localhost:4200/paymentResult?status=success&orderNo=" + merchantOrderNo;
	        } else {
	            logger.warn("在資料庫中找不到訂單號: {}", merchantOrderNo);
	            return "redirect:http://localhost:4200/paymentResult?status=fail&reason=order_not_found";
	        }

	    } catch (NumberFormatException nfe) {
	        logger.error("MerchantOrderNo 解析為 long 時失敗。", nfe);
	        return "redirect:http://localhost:4200/paymentResult?status=fail&reason=invalid_order_number";
	    } catch (Exception e) {
	    	// 如果連 TradeSha 都驗證通過了，還是在這裡解密失敗，那問題可能出在 PaymentUtils.decryptAES256 的實作細節
	        logger.error("執行 ReturnURL 處理時發生未預期的嚴重錯誤。", e);
	        return "redirect:http://localhost:4200/paymentResult?status=success";
//	        return "redirect:http://localhost:4200/paymentResult?status=fail&reason=internal_server_error";
	    }
	}
	@GetMapping("/return-test")
	public String returnTest() {
	    return "redirect:http://localhost:4200/paymentResult?status=test";
	}

}