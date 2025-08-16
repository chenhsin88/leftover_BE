package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.OrdersToReviewRes;
import com.example.leftovers.vo.ReviewVo;
import com.example.leftovers.vo.ReviewsCreateReq;
import com.example.leftovers.vo.ReviewsGetByMerchantsRes;
import com.example.leftovers.vo.ReviewsUpdateMerchantReplyReq;

public interface ReviewsService {

	public BasicRes create(ReviewsCreateReq req);
	
	public ReviewsGetByMerchantsRes getMerchantsReviewsByMerchants(int merchantId);
	
	public BasicRes ReviewsUpdateMerchantReply(long orderId, String userName, int merchantId, ReviewsUpdateMerchantReplyReq req);
	
	public BasicRes ReviewsDeleteMerchantReply(long orderId, String userName, int merchantId);
	
	public OrdersToReviewRes getOrdersToBeReviewed(String email);
	
	public ReviewVo getReviewByOrderAndUser(long orderId, String userEmail);
}
