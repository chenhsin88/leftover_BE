package com.example.leftovers.vo;

import java.util.List;

public class HistoryGetAllPriceByMerchantIdVo {

	private int merchantId;
	private List<HistoryGetAllByYearRevenueVo> yearRevenue;
	private List<HistoryGetAllByMonthlyRevenueVo> monthlyRevenue;

	public HistoryGetAllPriceByMerchantIdVo() {
		super();
	}

	public HistoryGetAllPriceByMerchantIdVo(int merchantId, List<HistoryGetAllByYearRevenueVo> yearRevenue,
			List<HistoryGetAllByMonthlyRevenueVo> monthlyRevenue) {
		super();
		this.merchantId = merchantId;
		this.yearRevenue = yearRevenue;
		this.monthlyRevenue = monthlyRevenue;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public List<HistoryGetAllByYearRevenueVo> getYearRevenue() {
		return yearRevenue;
	}

	public void setYearRevenue(List<HistoryGetAllByYearRevenueVo> yearRevenue) {
		this.yearRevenue = yearRevenue;
	}

	public List<HistoryGetAllByMonthlyRevenueVo> getMonthlyRevenue() {
		return monthlyRevenue;
	}

	public void setMonthlyRevenue(List<HistoryGetAllByMonthlyRevenueVo> monthlyRevenue) {
		this.monthlyRevenue = monthlyRevenue;
	}

}
