package com.example.leftovers.constants;

public class PaymentConstants {

    public static final String MERCHANT_ID = "";
    public static final String HASH_KEY = "";
    public static final String HASH_IV = "";

    // 🔁 藍新金流「MPG 串接」網址（正式環境）
    public static final String PAYMENT_URL = "https://ccore.newebpay.com/MPG/mpg_gateway";

    // ✅ 使用者付款完後返回你前端畫面的網址
    public static final String CLIENT_BACK_URL = "http://localhost:4200/main";

//    // ✅ 使用者付款完成後，藍新會呼叫你後端 API 的通知網址
//    public static final String NOTIFY_URL = "https://f38475575d74.ngrok-free.app/api/payment/notify";
//
//    // ✅ 與 NOTIFY_URL 可用相同網址（可選）
//    public static final String RETURN_URL = "https://f38475575d74.ngrok-free.app/api/payment/return";
}
