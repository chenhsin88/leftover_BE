package com.example.leftovers.vo;

public class HistoryGetAllByYearRevenueVo {

    private int year;  // 改成 int 年份

    private Long revenue;

    public HistoryGetAllByYearRevenueVo() {
        super();
    }

	public HistoryGetAllByYearRevenueVo(int year, Long revenue) {
		super();
		this.year = year;
		this.revenue = revenue;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}

   
}
