package com.example.leftovers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.leftovers.service.ifs.HistoryOrderService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.HistoryCreateReq;
import com.example.leftovers.vo.HistoryGetAllByMerchantIdRes;
import com.example.leftovers.vo.HistoryGetAllByUserEmailRes;
import com.example.leftovers.vo.HistoryGetAllPriceByMerchantIdRes;
import com.example.leftovers.vo.HistoryOrderGetByOrderIdRes;

@RestController
@RequestMapping("/historyOrder")
public class HistoryOrderController {

	 @Autowired
	    private HistoryOrderService historyOrderService;

//	    @PostMapping("/create")
//	    public ResponseEntity<BasicRes> HistoryOrderCreate(@RequestBody HistoryCreateReq req) {
//	    	 BasicRes response = historyOrderService.HistoryOrderCreate(req);
//	    	 return ResponseEntity.status(HttpStatus.OK).body(response);
//	    }

	    // 查詢歷史訂單
	    @GetMapping("/getHistoryOrder/{orderId}")
	    public HistoryOrderGetByOrderIdRes getHistoryOrder(@PathVariable("orderId") String orderId) {
	        return historyOrderService.HistoryOrderGetByOrderId(orderId);
	    }
	    
	    //查詢使用者所有歷史訂單
	    @GetMapping("/getUserAllOrder/{userEmail}")
	    public HistoryGetAllByUserEmailRes getUserHistoryOrders(@PathVariable ("userEmail")String userEmail) {
	        return historyOrderService.HistoryGetAllByUserEmail(userEmail);
	    }
	    //查詢商家歷史訂單
	    @GetMapping("/getMerchantUserAllOrder/{merchantId}")
	    public HistoryGetAllByMerchantIdRes HistoryGetAllByMerchantId(@PathVariable ("merchantId")int merchantId) {
	    	return historyOrderService.HistoryGetAllByMerchantId(merchantId);
	    }
	    
	  //查詢商家營收資料
	    @GetMapping("/getAllRevenueHistory/{merchantId}")
	    public HistoryGetAllPriceByMerchantIdRes HistoricalRevenue(@PathVariable ("merchantId")int merchantId) {
	    	return historyOrderService.HistoricalRevenue(merchantId);
	    }
	    
}
