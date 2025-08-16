package com.example.leftovers.service.impl;

import com.example.leftovers.constants.PaymentConstants;
import com.example.leftovers.exception.PaymentException;
import com.example.leftovers.service.ifs.PaymentService;
import com.example.leftovers.vo.PaymentRequestVo;
//import jakarta.xml.bind.DatatypeConverter;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

	 @Value("${app.public-url}")
	    private String publicUrl;
	
    @Override
    public PaymentRequestVo createPaymentOrder(String orderNo, int amount, String itemDesc) {
        try {
            // 訂單參數
            Map<String, String> paramMap = new LinkedHashMap<>();
            paramMap.put("MerchantID", PaymentConstants.MERCHANT_ID);
            paramMap.put("RespondType", "JSON");
            paramMap.put("TimeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            paramMap.put("Version", "2.0");
            paramMap.put("MerchantOrderNo", orderNo);
            paramMap.put("Amt", String.valueOf(amount));
            paramMap.put("ItemDesc", itemDesc);
            paramMap.put("ReturnURL", publicUrl + "/api/payment/return");
            paramMap.put("NotifyURL", publicUrl + "/api/payment/notify");
            paramMap.put("ClientBackURL", "http://localhost:4200/main"); // 返回頁面

            // 1. 組成 query string
            StringBuilder query = new StringBuilder();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            query.setLength(query.length() - 1);

            // 2. AES256 加密
            String tradeInfo = encryptAES256(query.toString(), PaymentConstants.HASH_KEY, PaymentConstants.HASH_IV);

            // 3. SHA256 雜湊
            String toSha = String.format("HashKey=%s&%s&HashIV=%s", PaymentConstants.HASH_KEY, tradeInfo, PaymentConstants.HASH_IV);
            String tradeSha = sha256(toSha).toUpperCase();

            // 建立回傳物件
            PaymentRequestVo vo = new PaymentRequestVo();
            vo.setMerchantID(PaymentConstants.MERCHANT_ID);
            vo.setTradeInfo(tradeInfo);
            vo.setTradeSha(tradeSha);
            vo.setVersion("2.0");

            return vo;

        } catch (Exception e) {
            throw new PaymentException("建立金流訂單失敗: " + e.getMessage());
        }
    }

    // AES-256 CBC 加密
    private String encryptAES256(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String encryptedHex = HexFormat.of().formatHex(encrypted);
        System.out.println(">>> Sending TradeInfo to NewebPay: " + encryptedHex);
     // 然後才真的去送出 request

//        return DatatypeConverter.printHexBinary(encrypted).toLowerCase();
        return HexFormat.of().formatHex(encrypted).toLowerCase();
    }

    // SHA256 雜湊
    private String sha256(String str) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
