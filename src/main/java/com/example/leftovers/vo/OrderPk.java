package com.example.leftovers.vo;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class OrderPk implements Serializable{

	private long orderId;
	
	private String pickupCode;

	public OrderPk() {
		super();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPickupCode() {
		return pickupCode;
	}

	public void setPickupCode(String pickupCode) {
		this.pickupCode = pickupCode;
	}

	public OrderPk(long orderId, String pickupCode) {
		super();
		this.orderId = orderId;
		this.pickupCode = pickupCode;
	}
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof OrderPk)) return false;
	        OrderPk that = (OrderPk) o;
	        return orderId == that.orderId &&
	               Objects.equals(pickupCode, that.pickupCode);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(orderId, pickupCode);
	    }
	
}
