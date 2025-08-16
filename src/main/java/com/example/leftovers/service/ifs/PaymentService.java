package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.PaymentRequestVo;


public interface PaymentService {


    public PaymentRequestVo createPaymentOrder(String orderNo, int amount, String itemDesc);


}
