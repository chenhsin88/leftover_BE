package com.example.leftovers.vo;

import java.util.List;

public class OrderGetByMechantIdRes extends BasicRes{
	private List<OrderSimpleVo> orders;

	public OrderGetByMechantIdRes() {
	}

	public OrderGetByMechantIdRes(int code, String message) {
		super(code, message);
	}

	public OrderGetByMechantIdRes(List<OrderSimpleVo> orders) {
		this.orders = orders;
	}

	public List<OrderSimpleVo> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderSimpleVo> orders) {
		this.orders = orders;
	}
}
