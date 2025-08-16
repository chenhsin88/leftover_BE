package com.example.leftovers.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leftovers.entity.HistoryOrder;
import com.example.leftovers.vo.HistoryGetAllByMonthlyRevenueVo;
import com.example.leftovers.vo.HistoryGetAllByYearRevenueVo;
import java.util.List; // 確保引入 List
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.leftovers.entity.HistoryOrder;

public interface HistoryOrderDao extends JpaRepository<HistoryOrder, Integer> {

	List<HistoryOrder> findByOrderId(String orderId);

	List<HistoryOrder> findByUserEmail(String userEmail);

	List<HistoryOrder> findByMerchantId(int merchantId);
	
	List<HistoryOrder> findAllByOrderIdIn(List<String> orderIds);
	@Query("SELECT FUNCTION('DATE_FORMAT', h.createdAt, '%Y-%m'), SUM(h.foodPrice * h.quantity) "
			+ "FROM HistoryOrder h " + "WHERE h.merchantId = :merchantId AND FUNCTION('YEAR', h.createdAt) = :year "
			+ "GROUP BY FUNCTION('DATE_FORMAT', h.createdAt, '%Y-%m')")
	List<Object[]> findMonthlyRevenueByMerchantIdAndYear(@Param("merchantId") int merchantId, @Param("year") int year);

	@Query("SELECT YEAR(h.createdAt), SUM(h.foodPrice * h.quantity) "
			+ "FROM HistoryOrder h WHERE h.merchantId = :merchantId " + "GROUP BY YEAR(h.createdAt)")
	List<Object[]> findYearRevenueByMerchantId(@Param("merchantId") int merchantId);

	long countByMerchantId(int merchantId);

	// +++ 請在此處新增以下這一行方法 +++
	/**
	 * 根據 orderId (String類型) 檢查是否存在任何紀錄。 Spring Data JPA 會自動為您實現這個方法。
	 * 
	 * @param orderId 這是帶有 "RF" 前綴的訂單編號字串
	 * @return 如果存在則回傳 true，否則回傳 false
	 */
	boolean existsByOrderId(String orderId);
	/**
	 * 根據使用者 Email 和訂單狀態來查詢歷史訂單。
	 * Spring Data JPA 會自動產生對應的 SQL 查詢。
	 * * @param userEmail 使用者的電子郵件
	 * @param status 訂單狀態 (例如 "completed")
	 * @return 符合條件的訂單列表
	 */
	List<HistoryOrder> findByUserEmailAndStatus(String userEmail, String status);

	 /**
     * 根據純數字的 orderId 查詢歷史訂單。
     * 使用 LIKE 語句來匹配字串 ID (例如 "RF" + 數字)。
     * @param numericOrderId 純數字的訂單 ID
     * @return 符合條件的歷史訂單
     */
    @Query("FROM HistoryOrder ho WHERE ho.orderId LIKE CONCAT('%', :numericOrderId)")
    Optional<HistoryOrder> findByNumericOrderId(@Param("numericOrderId") long numericOrderId);
}

