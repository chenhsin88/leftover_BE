package com.example.leftovers.vo;

public class OrderCreateRes extends BasicRes {

    private long orderId;

    public OrderCreateRes() {
        super();
    }

    public OrderCreateRes(int code, String message, long orderId) {
        super(code, message); // ✅ 這裡改正
        this.orderId = orderId;
    }
    public OrderCreateRes(int code, String message) {
        super(code, message); // ✅ 這裡改正
    }

    public OrderCreateRes(long orderId) {
        super();
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}

