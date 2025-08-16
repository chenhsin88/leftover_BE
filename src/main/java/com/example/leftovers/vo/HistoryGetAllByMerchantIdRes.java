package com.example.leftovers.vo;


import java.util.List;

public class HistoryGetAllByMerchantIdRes extends BasicRes{
	
	private List<HistoryGetMerchantAllOrderVo> historyGetMerchantAllOrder;

	public HistoryGetAllByMerchantIdRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HistoryGetAllByMerchantIdRes(int code, String message,List<HistoryGetMerchantAllOrderVo> historyGetMerchantAllOrder) {
		super(code,message);
		this.historyGetMerchantAllOrder = historyGetMerchantAllOrder;
	}

	public List<HistoryGetMerchantAllOrderVo> getHistoryGetMerchantAllOrder() {
		return historyGetMerchantAllOrder;
	}

	public void setHistoryGetMerchantAllOrder(List<HistoryGetMerchantAllOrderVo> historyGetMerchantAllOrder) {
		this.historyGetMerchantAllOrder = historyGetMerchantAllOrder;
	}
	
    
    
}
