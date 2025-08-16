package com.example.leftovers.vo;

import java.util.List;

public class HistoryOrderGetByOrderIdRes extends BasicRes{

	private List<HistoryOrderVo> historyOrderlist;

	public HistoryOrderGetByOrderIdRes(List<HistoryOrderVo> historyOrderlist) {
		super();
		this.historyOrderlist = historyOrderlist;
	}

	public HistoryOrderGetByOrderIdRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HistoryOrderGetByOrderIdRes(int code, String message, List<HistoryOrderVo> historyOrderList) {
		super(code, message);
		this.historyOrderlist = historyOrderList;
	}

	public List<HistoryOrderVo> getHistoryOrderlist() {
		return historyOrderlist;
	}

	public void setHistoryOrderlist(List<HistoryOrderVo> historyOrderlist) {
		this.historyOrderlist = historyOrderlist;
	}



	
	
}
