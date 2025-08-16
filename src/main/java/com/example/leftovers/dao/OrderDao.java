
package com.example.leftovers.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leftovers.entity.Merchants;
import com.example.leftovers.entity.Orders;
import com.example.leftovers.vo.OrderPk;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public interface OrderDao extends JpaRepository<Orders, OrderPk> {
	Optional<Orders> findByOrderId(long orderId);
	
	List<Orders> findAllByUserEmail(String userEmail);

	List<Orders> findOrdersByMerchantsId(int merchantsId);

	@Modifying
	@Transactional
	@Query("UPDATE Orders o SET o.status = :status WHERE o.orderId = :orderId AND o.pickupCode = :pickupCode")
	int updateStatusByIdAndPickupCode(@Param("orderId") long orderId, @Param("pickupCode") String pickupCode,
			@Param("status") String status);

	List<Orders> findByOrderedAtBefore(LocalDateTime time);
	
	boolean existsByPickupCode(String pickupCode);
	
	@Query("SELECT o.orderId FROM Orders o WHERE o.orderId BETWEEN :start AND :end")
	List<Long> findByOrderIdBetween(@Param("start") long start, @Param("end") long end);
	
	List<Merchants> findMerchantByOrderId(long orderId);
}
