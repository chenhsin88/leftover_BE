package com.example.leftovers.vo;

public class OrderRejectReq {
	
	private long orderId;

	private String rejectReason;
	



	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}


	
	
	//private LocalDateTime rejectAt;
	
}
