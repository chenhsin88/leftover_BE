package com.example.leftovers.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.aspectj.weaver.bcel.BcelObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leftovers.entity.PushNotificationTokens;
import com.example.leftovers.vo.PushNotificationTokensDto;
import com.example.leftovers.vo.PushNotificationTokensUserDto;
import com.example.leftovers.vo.PushNotificationTokensUserVo;

import jakarta.transaction.Transactional;

public interface PushNotificationTokensDao extends JpaRepository<PushNotificationTokens, Integer> {

	// 畫面資料
//	@Query("SELECT new com.example.leftovers.vo.PushNotificationTokensDto(f.name, f.imageUrl, f.originalPrice, f.discountedPrice, f.pickupEndTime) "
//			+ "FROM FoodItems f WHERE f.merchantsId = :merchantId AND f.isActive = true")
	@Query("SELECT new com.example.leftovers.vo.PushNotificationTokensDto(" + "f.id, f.name, f.imageUrl, "
			+ " COALESCE(f.originalPrice, 0), " + " COALESCE(f.discountedPrice, 0), " + " f.pickupEndTime, "
			+ " COALESCE(t.newPrice, 0), " + " COALESCE(t.defaultHours, 0), " + " t.setTime, "
			+ " COALESCE(t.pushNotifications, false) ) "
			+ " FROM FoodItems f JOIN PushNotificationTokens t ON f.id = t.foodItemsId "
			+ " WHERE f.merchantsId = :merchantId AND f.isActive = true")
	public List<PushNotificationTokensDto> UIData(@Param("merchantId") int merchantsId);

	// 修改foodItem價格
	@Transactional
	@Modifying
	@Query(value = "UPDATE food_items SET discounted_price = :newPrice WHERE id = :foodItemId", nativeQuery = true)
	public void setPrice(@Param("newPrice") int newPrice, @Param("foodItemId") int foodItemId);

	// 修改最新優惠價格
	@Modifying
	@Transactional
	@Query(value = "UPDATE push_notification_tokens SET new_price = :newPrice WHERE food_items_id = :foodItemId", nativeQuery = true)
	public void newPrice(@Param("newPrice") int newPrice, @Param("foodItemId") int foodItemId);

	// 推播開關
	@Transactional
	@Modifying
	@Query(value = "UPDATE push_notification_tokens SET push_notifications = ?2 WHERE food_items_id = ?1", nativeQuery = true)
	public void toggle(int foodItemId, boolean isActive);

	// 設定時間
	@Transactional
	@Modifying
	@Query(value = "UPDATE push_notification_tokens SET set_time = ?3, default_hours = ?2 WHERE food_items_id = ?1", nativeQuery = true)
	public void setTime(int foodItemId, int defaultHours, LocalDateTime setTime);

//	@Query(value = "SELECT discounted_price FROM push_notification_tokens WHERE food_items_id = ?1", nativeQuery = true)
//	public int findByDiscountedPrice(int foodItemId);
	// 上一次的折扣價格
	@Query(value = "SELECT previous_price FROM push_notification_tokens WHERE food_items_id = ?1", nativeQuery = true)
	public int findPreviousPriceByFoodItemId(int foodItemId);

	// 新增所有可推播的項目
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO push_notification_tokens (merchants_id, previous_price, food_items_id) "
			+ " SELECT fi.merchants_id, fi.discounted_price, fi.id " + "FROM food_items fi "
			+ " WHERE fi.is_active = TRUE "
			+ " AND fi.id NOT IN (SELECT food_items_id FROM push_notification_tokens)", nativeQuery = true)
	void getAndCreateData();

	// 抓取時間截止時間
	@Query(value = "SELECT pickup_end_time FROM food_items WHERE id = ?1", nativeQuery = true)
	LocalDateTime getPickupEndTime(int foodItemId);

	// 時間偵測自動修改價格
	@Query("SELECT p FROM PushNotificationTokens p WHERE p.setTime <= :now AND p.newPrice IS NOT NULL")
	List<PushNotificationTokens> findTokensToUpdate(@Param("now") LocalDateTime now);

	// 使用者收到推播的資料
    @Query(value = "SELECT " +
            "p.food_items_id AS foodItemsId, " +
            "f.name AS foodName, " +
            "f.category AS category, " +
            "f.description AS description, " +
            "f.original_price AS originalPrice, " +
            "p.previous_price AS discountedPrice, " +
            "f.image_url AS imageUrl, " +
            "p.set_time AS setTime, " +
            "f.pickup_end_time AS pickupTime, " +
            "p.new_price AS newPrice, " +
            "m.merchants_id AS merchantsId, " +
            "m.name AS merchantName, " +
            "m.address_text AS addressText, " +
            "m.longitude_and_latitude AS longitudeAndLatitude " +
            "FROM push_notification_tokens p " +
            "JOIN food_items f ON p.food_items_id = f.id " +
            "JOIN merchants m ON p.merchants_id = m.merchants_id " +
            "WHERE p.push_notifications = true",
            nativeQuery = true)
    List<PushNotificationTokensUserDto> getUserUi();

}
