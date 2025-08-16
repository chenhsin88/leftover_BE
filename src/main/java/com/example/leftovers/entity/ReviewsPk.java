package com.example.leftovers.entity;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class ReviewsPk implements Serializable {

    private long orderId;
    private String userName;
    private int merchantId;
    
    public ReviewsPk() {
    }

    public ReviewsPk(long orderId, String userName,int merchantId) {
        this.orderId = orderId;
        this.userName = userName;
        this.merchantId = merchantId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    
    
    public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	//JPA 使用這兩個方法來比對主鍵是否相等。
    //如果不實作，JPA 無法正確使用 ReviewsPk 查詢或比對實體對象，導致 findById()、deleteById()、save() 失效或錯誤。
    // ✅ 必須實作 equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewsPk)) return false;
        ReviewsPk that = (ReviewsPk) o;
        return orderId == that.orderId &&
                merchantId == that.merchantId &&
                Objects.equals(userName, that.userName);
    }

    // ✅ 必須實作 hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(orderId, userName,merchantId);
    }
}

