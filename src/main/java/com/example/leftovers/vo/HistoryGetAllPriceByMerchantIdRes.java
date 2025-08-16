package com.example.leftovers.vo;

public class HistoryGetAllPriceByMerchantIdRes extends BasicRes {

	private HistoryGetAllPriceByMerchantIdVo historyGetAllPriceByMerchantIdVo;

	public HistoryGetAllPriceByMerchantIdRes() {
		super();
	}

	public HistoryGetAllPriceByMerchantIdRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public HistoryGetAllPriceByMerchantIdRes(int code, String message,
			HistoryGetAllPriceByMerchantIdVo historyGetAllPriceByMerchantIdVo) {
		super(code, message);
		this.historyGetAllPriceByMerchantIdVo = historyGetAllPriceByMerchantIdVo;
	}

	public HistoryGetAllPriceByMerchantIdVo getHistoryGetAllPriceByMerchantIdVo() {
		return historyGetAllPriceByMerchantIdVo;
	}

	public void setHistoryGetAllPriceByMerchantIdVo(HistoryGetAllPriceByMerchantIdVo historyGetAllPriceByMerchantIdVo) {
		this.historyGetAllPriceByMerchantIdVo = historyGetAllPriceByMerchantIdVo;
	}

}
