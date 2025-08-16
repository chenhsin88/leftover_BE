package com.example.leftovers.vo;

public class PaymentRequestVo {
	private String MerchantID;
	private String TradeInfo; // AES256 加密後參數
	private String TradeSha;  // SHA256 驗證用
	private String Version;

	public String getMerchantID() {
		return MerchantID;
	}

	public void setMerchantID(String merchantID) {
		MerchantID = merchantID;
	}

	public String getTradeInfo() {
		return TradeInfo;
	}

	public void setTradeInfo(String tradeInfo) {
		TradeInfo = tradeInfo;
	}

	public String getTradeSha() {
		return TradeSha;
	}

	public void setTradeSha(String tradeSha) {
		TradeSha = tradeSha;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}
}
