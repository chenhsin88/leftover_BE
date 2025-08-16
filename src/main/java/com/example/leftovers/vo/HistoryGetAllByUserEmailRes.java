package com.example.leftovers.vo;

import java.util.List;

public class HistoryGetAllByUserEmailRes extends BasicRes{

	private List<HistoryGetUserAllOrderVo> historyGetUserAllOrderVo;

	public HistoryGetAllByUserEmailRes() {
		super();
	}

	public HistoryGetAllByUserEmailRes(int code, String message,List<HistoryGetUserAllOrderVo> historyGetUserAllOrderVo) {
		super(code, message);
		this.historyGetUserAllOrderVo = historyGetUserAllOrderVo;
	}

	public List<HistoryGetUserAllOrderVo> getHistoryGetUserAllOrderVo() {
		return historyGetUserAllOrderVo;
	}

	public void setHistoryGetUserAllOrderVo(List<HistoryGetUserAllOrderVo> historyGetUserAllOrderVo) {
		this.historyGetUserAllOrderVo = historyGetUserAllOrderVo;
	}
	
	
}
