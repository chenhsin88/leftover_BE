package com.example.leftovers.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leftovers.entity.Reviews;
import com.example.leftovers.entity.ReviewsPk;
import com.example.leftovers.vo.ReviewVo; 


public interface ReviewsDao extends JpaRepository<Reviews, ReviewsPk>{


	 Optional<Reviews> findByOrderIdAndUserName(long orderId, String userName);

	 List<Reviews> findByMerchantId(int merchantId); // 用於 get by merchant 功能
	 
	 @Query("SELECT new com.example.leftovers.vo.ReviewVo(" +
	           "r.rating, r.comment, r.userName, r.createdAt, r.merchantReply, r.merchantReplyAt, u.profilePictureUrl) " +
	           "FROM Reviews r " +
	           "JOIN Orders o ON r.orderId = o.orderId " +
	           "JOIN User u ON o.userEmail = u.email " +
	           "WHERE r.merchantId = :merchantId")
	    List<ReviewVo> findReviewsWithUserAvatarByMerchantId(@Param("merchantId") int merchantId);
	
	 /**
	  * 透過跨表查詢，根據使用者的 email 找出其所有留下的評論。
	  * 這是為了找出使用者已經評論過的所有訂單。
	  * @param email 使用者的唯一電子郵件
	  * @return 該使用者的所有評論列表
	  */
	 @Query("SELECT r FROM Reviews r JOIN Orders o ON r.orderId = o.orderId WHERE o.userEmail = :email")
	 List<Reviews> findReviewsByUserEmail(@Param("email") String email);
	 
	 @Query("SELECT r FROM Reviews r JOIN Orders o ON r.orderId = o.orderId WHERE r.orderId = :orderId AND o.userEmail = :email")
	    Optional<Reviews> findReviewByOrderIdAndUserEmail(@Param("orderId") long orderId, @Param("email") String email);

	 /**
	     * 根據 orderId (long 類型) 檢查是否存在任何評論紀錄。
	     * Spring Data JPA 會自動為您實現這個方法。
	     * @param orderId 訂單的純數字 ID
	     * @return 如果存在則回傳 true，否則回傳 false
	     */
	    boolean existsByOrderId(long orderId);
}

