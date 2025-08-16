package com.example.leftovers.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.leftovers.vo.OrdersToReviewRes;
import com.example.leftovers.vo.ReviewVo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.leftovers.service.ifs.ReviewsService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.ReviewsCreateReq;
import com.example.leftovers.vo.ReviewsGetByMerchantsRes;
import com.example.leftovers.vo.ReviewsUpdateMerchantReplyReq;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
@RestController
@RequestMapping("/reviews")
public class ReviewsController {

	 @Autowired
	    private ReviewsService reviewsService;
	 
	 
	 // 新增評論
	    @PostMapping("/create")
	    public BasicRes createReview(@RequestBody @Valid ReviewsCreateReq req) {
	        return reviewsService.create(req);
	    }

	    // 根據商家名稱查詢評論
	    @GetMapping("/merchant")
	    public ReviewsGetByMerchantsRes getReviewsByMerchant(@RequestParam("merchants") int merchantId) {
	        return reviewsService.getMerchantsReviewsByMerchants(merchantId);
	    }
	    
	    @PostMapping("/{orderId}/{userName}/{merchantId}/reply")
	    public BasicRes updateMerchantReply(
	            @PathVariable ("orderId")long orderId,
	            @PathVariable ("userName")String userName,
	            @PathVariable ("merchantId")int merchantId,
	            @RequestBody ReviewsUpdateMerchantReplyReq req) {
	        return reviewsService.ReviewsUpdateMerchantReply(orderId, userName, merchantId, req);
	    }

	    @PostMapping("/{orderId}/{userName}/{merchantId}/delete")
	    public BasicRes deleteMerchantReply(
	    		@PathVariable ("orderId")long orderId,
	            @PathVariable ("userName")String userName,
	            @PathVariable ("merchantId")int merchantId) {
	        return reviewsService.ReviewsDeleteMerchantReply(orderId, userName, merchantId);
	    }
	    
	    /**
	     * 根據 email 獲取該使用者可以進行評論的訂單列表
	     * @param email 使用者的電子郵件
	     * @return 一個包含待評論訂單的列表
	     */
	    @GetMapping("/to-be-reviewed/{email}")
	    public OrdersToReviewRes getOrdersToBeReviewed(@PathVariable("email") String email) {
	        return reviewsService.getOrdersToBeReviewed(email);
	    }
	    /**
	     * 根據訂單 ID 和使用者 Email 查詢單筆評論詳情
	     * @param orderId 訂單的純數字 ID
	     * @param userEmail 使用者的電子郵件
	     * @return 評論詳情，如果找不到則回傳特定訊息
	     */
	    @GetMapping("/order/{orderId}/{userEmail}")
	    public ReviewVo getReviewByOrderAndUser(
	        @PathVariable("orderId") long orderId,
	        @PathVariable("userEmail") String userEmail
	    ) {
	        return reviewsService.getReviewByOrderAndUser(orderId, userEmail);
	    }
}
