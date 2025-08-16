package com.example.leftovers.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.leftovers.dao.HistoryOrderDao;
import com.example.leftovers.dao.ReviewsDao;
import com.example.leftovers.entity.HistoryOrder;
import com.example.leftovers.entity.Reviews;
import com.example.leftovers.entity.ReviewsPk;
import com.example.leftovers.service.ifs.ReviewsService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.ReviewsCreateReq;
import com.example.leftovers.vo.ReviewsGetByMerchantsRes;
import com.example.leftovers.vo.ReviewsUpdateMerchantReplyReq;
import com.example.leftovers.vo.ReviewsVo;
import com.example.leftovers.vo.OrderToReviewVo; // <-- 新增
import com.example.leftovers.vo.OrdersToReviewRes;
import com.example.leftovers.vo.ReviewVo;


@Service
public class ReviewsImpl implements ReviewsService {

	@Autowired
	private ReviewsDao reviewsDao;
	
	@Autowired
    private HistoryOrderDao historyOrderDao;
	
	@Override
	public BasicRes create(ReviewsCreateReq req) {
		// 先檢查是否已存在該 orderId 與 user 的評論
		// 直接檢查這個 orderId 是否已經存在於 reviews 資料表中。
				// 這比用 userName 更可靠，因為一個訂單理論上只能被評論一次。
				if (reviewsDao.existsByOrderId(req.getOrderId())) {
					return new BasicRes(400, "您已對此訂單評價過，無法重複評論");
				}
		Reviews reviews = new Reviews();
		reviews.setMerchant(req.getMerchants());
		reviews.setRating(req.getRating());
		reviews.setOrderId(req.getOrderId());
		reviews.setUserName(req.getUserName());
		reviews.setCreatedAt(LocalDateTime.now());
		// 如果不是 null，就用它原本的值；如果是 null，就改用一個空字串 ""。
	    reviews.setComment(req.getComment() != null ? req.getComment() : "");
		reviews.setMerchantId(req.getMerchantId());
		reviewsDao.save(reviews);
		return new BasicRes(200, "評論新增成功");
	}

	@Override
	public ReviewsGetByMerchantsRes getMerchantsReviewsByMerchants(int merchantId) {
		List<Reviews> result = reviewsDao.findByMerchantId(merchantId);

		if (result.isEmpty()) {
			return new ReviewsGetByMerchantsRes(204, "查無評論", List.of());
		}

		List<ReviewsVo> voList = result.stream().map(r -> {
			ReviewsVo vo = new ReviewsVo();
			vo.setMerchant(r.getMerchant());
			vo.setRating(r.getRating());
			vo.setComment(r.getComment());
			vo.setCreatedAt(r.getCreatedAt());
			vo.setUserName(r.getUserName());
			vo.setOrderId(r.getOrderId());
			vo.setMerchantId(r.getMerchantId());
			vo.setMerchantReply(r.getMerchantReply());
			vo.setMerchantReplyAt(r.getMerchantReplyAt());
			
			return vo;
		}).collect(Collectors.toList());

		return new ReviewsGetByMerchantsRes(200, "查詢成功", voList);
	}

	@Override
	public BasicRes ReviewsUpdateMerchantReply(long orderId, String userName, int merchantId,
			ReviewsUpdateMerchantReplyReq req) {

		ReviewsPk pk = new ReviewsPk(orderId, userName, merchantId);

		Optional<Reviews> optionalReview = reviewsDao.findById(pk);

		if (optionalReview.isEmpty()) {
			return new BasicRes(404, "找不到該筆評論，無法回覆");
		}

		Reviews reviews = optionalReview.get();

		// 只更新商家回覆欄位
		reviews.setMerchantReply(req.getMerchantReply());
		reviews.setMerchantReplyAt(LocalDateTime.now());

		// 這裡如果有 @UpdateTimestamp，更新時間會自動更新，不需手動設
		reviewsDao.save(reviews);

		return new BasicRes(200, "回覆成功");
	}

	@Override
	public BasicRes ReviewsDeleteMerchantReply(long orderId, String userName, int merchantId) {
		ReviewsPk pk = new ReviewsPk(orderId, userName, merchantId);
		Optional<Reviews> optional = reviewsDao.findById(pk);

		if (optional.isPresent()) {
			Reviews review = optional.get();

			// 只清除賣家的回覆與時間
			review.setMerchantReply(null);
			review.setMerchantReplyAt(null);

			reviewsDao.save(review);

			return new BasicRes(200, "success");
		} else {
			return new BasicRes(404, "error");
		}
	}
	
	@Override
	public OrdersToReviewRes getOrdersToBeReviewed(String email) {
	    // 1. 找出該使用者所有 "completed" 的訂單
	    List<HistoryOrder> allCompletedOrders = historyOrderDao.findByUserEmailAndStatus(email, "completed");

	    if (allCompletedOrders.isEmpty()) {
	        return new OrdersToReviewRes(200, "沒有可評論的訂單", Collections.emptyList());
	    }

	    // 2. 【對應修改】直接根據 email 找出該使用者已經評論過的訂單ID
	    List<Long> reviewedOrderIds = reviewsDao.findReviewsByUserEmail(email).stream() // <-- 呼叫我們的新方法
	        .map(Reviews::getOrderId)
	        .collect(Collectors.toList());

	    // 3. 【篩選邏輯不變】找出已完成但尚未評論的訂單
	    List<OrderToReviewVo> ordersToReviewVos = allCompletedOrders.stream()
	        .filter(order -> {
	            String numericIdStr = order.getOrderId().substring(2);
	            long numericId = Long.parseLong(numericIdStr);
	            return !reviewedOrderIds.contains(numericId);
	        })
	        .map(order -> {
	                // 將篩選出的訂單轉換成我們剛才定義的 VO 格式
	                String numericIdStr = order.getOrderId().substring(2);
	                long numericId = Long.parseLong(numericIdStr);
	                return new OrderToReviewVo(
	                    numericId,
	                    order.getOrderId(), // 顯示用的 "RF..." ID
	                    order.getMerchant(),
	                    order.getCreatedAt(),
	                    order.getFoodName()
	                );
	            })
	            .collect(Collectors.toList());
	        
	        return new OrdersToReviewRes(200, "查詢成功", ordersToReviewVos);
	    }
	@Override
    public ReviewVo getReviewByOrderAndUser(long orderId, String userEmail) {
		 Optional<HistoryOrder> historyOrderOpt = historyOrderDao.findByNumericOrderId(orderId);

		    // 如果連歷史訂單都找不到，那肯定沒有評論
		    if (historyOrderOpt.isEmpty()) {
		        return null;
		    }

		    HistoryOrder historyOrder = historyOrderOpt.get();

		    // 安全性檢查：確認這筆訂單的使用者 email 與傳入的 email 一致
		    if (!historyOrder.getUserEmail().equals(userEmail)) {
		        // 如果不一致，代表此使用者無權查看這筆訂單的評論
		        return null;
		    }

		    // 從歷史訂單中取得絕對正確的 userName
		    String userName = historyOrder.getUserName();

		    // 步驟 2: 現在，使用最精準的「純數字 orderId」和「正確的 userName」去 reviews 表中查詢
		    Optional<Reviews> reviewOptional = reviewsDao.findByOrderIdAndUserName(orderId, userName);

        if (reviewOptional.isPresent()) {
            Reviews review = reviewOptional.get();
            // 如果找到了評論，就把它轉換成 ReviewVo 回傳
            ReviewVo vo = new ReviewVo();
            vo.setRating(review.getRating());
            vo.setComment(review.getComment());
            vo.setUserName(review.getUserName());
            vo.setCreatedAt(review.getCreatedAt());
            vo.setMerchantReply(review.getMerchantReply());
            vo.setMerchantReplyAt(review.getMerchantReplyAt());
            // 這裡可以回傳一個 code 或 message，但為了簡單起見，直接回傳 Vo
            return vo;
        } else {
            // 如果找不到評論，回傳 null 或一個帶有特定訊息的物件
            // 回傳 null 讓前端更容易判斷
            return null; 
        }
    }	
}
