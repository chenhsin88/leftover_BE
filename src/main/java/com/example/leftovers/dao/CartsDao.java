package com.example.leftovers.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leftovers.entity.Carts;

import jakarta.transaction.Transactional;

public interface CartsDao extends JpaRepository<Carts, Integer> {

	List<Carts> findByUserEmail(String email);

	List<Carts> findAllByUserEmailAndFoodItemId(String userEmail, int foodItemId);

	List<Carts> findByCreatedAtBefore(LocalDateTime time);

   
	@Modifying
	@Transactional
	@Query("DELETE FROM Carts c WHERE c.userEmail = :userEmail AND c.merchantsId = :merchantId AND c.foodItemId IN :foodItemId")
	void deleteMultipleItems(@Param("userEmail") String userEmail,
	                         @Param("merchantId") int merchantId,
	                         @Param("foodItemId") List<Integer> foodItemId);
	
	Optional<Carts> findByUserEmailAndMerchantsIdAndFoodItemIdAndStatus(
		    String userEmail, int merchantsId, int foodItemId, String status);
}
