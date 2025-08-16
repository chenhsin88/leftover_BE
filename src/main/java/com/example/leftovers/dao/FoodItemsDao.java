package com.example.leftovers.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leftovers.entity.FoodItems;

public interface FoodItemsDao extends JpaRepository<FoodItems, Integer> {
	
	List<FoodItems> findByMerchantsIdIn(List<Integer> merchantIds);
	// 根據店家 ID 查找所有商品
	List<FoodItems> findByMerchantsId(int merchantsId);

	// 根據商品名稱模糊查詢（不區分大小寫）
	List<FoodItems> findByNameContainingIgnoreCase(String foodName);

	// 查找商品數量大於某個值的商品（例：庫存 > 0）
	List<FoodItems> findByQuantityAvailableGreaterThan(int quantity);

	// 根據分類查找商品
	List<FoodItems> findByCategory(String category);

	// 只查 quantityAvailable（數量）
    @Query("SELECT f.quantityAvailable FROM FoodItems f WHERE f.id = :id")
    Integer findQuantityAvailableById(@Param("id") int id);

    // 只查是否啟用（isActive）
    @Query("SELECT f.isActive FROM FoodItems f WHERE f.id = :id")
    Boolean findIsActiveById(@Param("id") int id);
    
    // 查詢商品名稱
    @Query("SELECT f.name FROM FoodItems f WHERE f.id = ?1")
    String findFoodNameById(int id);
    
    List<FoodItems> findByMerchantsIdAndIsActive(int merchantId, boolean isActive);
    
    @Query("SELECT f FROM FoodItems f WHERE f.id = :id AND f.merchantsId = :merchantsId")
    Optional<FoodItems> findByFoodIdAndMerchant(@Param("id") Integer foodId, @Param("merchantsId") Integer merchantsId);
}
