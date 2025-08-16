// 檔案路徑: src/main/java/com/example/leftovers/vo/OrdersToReviewRes.java
package com.example.leftovers.vo;

import java.util.List;

public class OrdersToReviewRes extends BasicRes {

    private List<OrderToReviewVo> orders;

    public OrdersToReviewRes(int code, String message, List<OrderToReviewVo> orders) {
        super(code, message);
        this.orders = orders;
    }

    public List<OrderToReviewVo> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderToReviewVo> orders) {
        this.orders = orders;
    }
}