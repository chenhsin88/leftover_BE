package com.example.leftovers.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
// 請確保您有引入這個 DatatypeConverter
//import jakarta.xml.bind.DatatypeConverter; 
import java.util.HexFormat;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

public class PaymentUtils {

    public static String decryptAES256(String encrypted, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // ✅ 修正的點：使用 Hex 解碼，與加密時的編碼方式保持一致
//        byte[] encryptedBytes = DatatypeConverter.parseHexBinary(encrypted);
        byte[] encryptedBytes = HexFormat.of().parseHex(encrypted);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        
        return new String(decryptedBytes, "UTF-8");
    }
    /**
     * NewebPay ReturnURL 版——密文前 16 bytes 即為隨機 IV
     */
    public static String decryptAES256WithDynamicIV(String encryptedHex,
                                                    String key) throws Exception {

        byte[] cipherBytes = java.util.HexFormat.of().parseHex(encryptedHex);

        // ① 取前 16 bytes 當 IV
        byte[] ivBytes   = java.util.Arrays.copyOfRange(cipherBytes, 0, 16);
        // ② 剩下才是真正密文
        byte[] bodyBytes = java.util.Arrays.copyOfRange(cipherBytes, 16, cipherBytes.length);

        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
        javax.crypto.spec.SecretKeySpec keySpec =
                new javax.crypto.spec.SecretKeySpec(key.getBytes(java.nio.charset.StandardCharsets.UTF_8), "AES");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec,
                    new javax.crypto.spec.IvParameterSpec(ivBytes));

        byte[] plainBytes = cipher.doFinal(bodyBytes);
        return new String(plainBytes, java.nio.charset.StandardCharsets.UTF_8);
    }
    /**
     * NewebPay ReturnURL 專用：丟掉前 16 Bytes，再用固定 HashIV 解密
     */
 // PaymentUtils.java
    public static String decryptAES256Skip16UseFixedIV(String encryptedHex,
                                                       String key,
                                                       String fixedIv) throws Exception {

        byte[] cipherAll  = HexFormat.of().parseHex(encryptedHex);
        byte[] bodyBytes  = Arrays.copyOfRange(cipherAll, 16, cipherAll.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec =
            new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec =
            new IvParameterSpec(fixedIv.getBytes(StandardCharsets.UTF_8));

        // ★★★ 必加的一行 ★★★
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] plainBytes = cipher.doFinal(bodyBytes);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }


}