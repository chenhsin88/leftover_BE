package com.example.leftovers.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.leftovers.entity.OrderFoodItem;


public interface OrderItemDao extends JpaRepository<OrderFoodItem, Integer>{
	  List<OrderFoodItem> findByOrderId(long orderId);
	  
	  List<OrderFoodItem> findByOrderedAtBefore(LocalDateTime deadline);
	  
	  
}
