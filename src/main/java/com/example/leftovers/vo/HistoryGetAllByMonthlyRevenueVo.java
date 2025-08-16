package com.example.leftovers.vo;

public class HistoryGetAllByMonthlyRevenueVo {

    private String month;  // 字串格式年月
    private Long revenue;   // 可能要改Long有機會報掉

    public HistoryGetAllByMonthlyRevenueVo() {
        super();
    }

	public HistoryGetAllByMonthlyRevenueVo(String month, Long revenue) {
		super();
		this.month = month;
		this.revenue = revenue;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}
    
    
}
