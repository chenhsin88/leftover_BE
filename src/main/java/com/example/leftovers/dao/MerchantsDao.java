package com.example.leftovers.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leftovers.entity.Merchants;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query; // 確保引入
import org.springframework.data.repository.query.Param;

public interface MerchantsDao extends JpaRepository<Merchants, Integer> {
	List<Merchants> findByCreatedByEmail(String createdByEmail);
	// 註冊
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO merchants ("
			+ " name, description, address_text, phone_number, contact_email, created_by_email,"//
			+ " logo_url,banner_image_url, opening_hours_description, map_screenshot_url,"//
			+ " map_google_url, is_active, approved_by_admin_id, approved_at, pickup_instructions, longitude_and_latitude) "//
			+ " VALUES ( :name, :description, :addressText, :phoneNumber, :contactEmail, :createdByEmail, "//
			+ " :logoUrl, :bannerImageUrl, :openingHoursDescription, :mapScreenshotUrl, :mapGoogleUrl,"//
			+ " :isActive, :approvedByAdminId, :approvedAt, :pickupInstructions, :longitudeAndLatitude)", nativeQuery = true)
	public void register(//
			@Param("name") String name, //
			@Param("description") String description, //
			@Param("addressText") String addressText, //
			@Param("phoneNumber") String phoneNumber, //
			@Param("contactEmail") String contactEmail, //
			@Param("createdByEmail") String createdByEmail, //
			@Param("logoUrl") String logoUrl, //
			@Param("bannerImageUrl") String bannerImageUrl, //
			@Param("openingHoursDescription") String openingHoursDescription, //
			@Param("mapScreenshotUrl") String mapScreenshotUrl, //
			@Param("mapGoogleUrl") String mapGoogleUrl, //
			@Param("isActive") boolean isActive, //
			@Param("approvedByAdminId") Integer approvedByAdminId, //
			@Param("approvedAt") LocalDateTime approvedAt, //
			@Param("pickupInstructions") String pickupInstructions,//
			@Param("longitudeAndLatitude") String longitudeAndLatitude);//

	//更新
	@Modifying
	@Transactional
	@Query(value = "UPDATE merchants SET "
	        + "name = :name, "
	        + "description = :description, "
	        + "address_text = :addressText, "
	        + "phone_number = :phoneNumber, "
	        + "contact_email = :contactEmail, "
	        + "logo_url = :logoUrl, "
	        + "banner_image_url = :bannerImageUrl, "
	        + "opening_hours_description = :openingHoursDescription, "
	        + "map_screenshot_url = :mapScreenshotUrl, "
	        + "map_google_url = :mapGoogleUrl, "
	        + "is_active = :isActive, "
	        + "approved_by_admin_id = :approvedByAdminId, "
	        + "approved_at = :approvedAt, "
	        + "pickup_instructions = :pickupInstructions ,"
	        + "longitude_and_latitude = :longitudeAndLatitude "
	        + "WHERE merchants_id = :merchantsId", nativeQuery = true)
	void updateMerchant(//
			@Param("name") String name, //
			@Param("description") String description, //
			@Param("addressText") String addressText, //
			@Param("phoneNumber") String phoneNumber, //
			@Param("contactEmail") String contactEmail, //
			@Param("logoUrl") String logoUrl, //
			@Param("bannerImageUrl") String bannerImageUrl, //
			@Param("openingHoursDescription") String openingHoursDescription, //
			@Param("mapScreenshotUrl") String mapScreenshotUrl, //
			@Param("mapGoogleUrl") String mapGoogleUrl, //
			@Param("isActive") boolean isActive, //
			@Param("approvedByAdminId") Integer approvedByAdminId, //
			@Param("approvedAt") LocalDateTime approvedAt, //
			@Param("pickupInstructions") String pickupInstructions,//
			@Param("longitudeAndLatitude") String longitudeAndLatitude,//
	 		@Param("merchantsId") int merchantsId);//

	//查詢店家名稱
	@Query(value = "SELECT name FROM merchants WHERE Merchants_id = ?1", nativeQuery = true)
	public String findMerchantsNameByMerchantsId(int MerchantsId);
	
	//檢查有沒有該店家(計算數量>0)
	@Query(value = "SELECT count(Merchants_id) FROM merchants WHERE Merchants_id = ?1", nativeQuery = true)
	public int findByMerchantsId(int MerchantsId);

	//檢查有沒有該管理員的信箱(計算數量>0)
	@Query(value = "SELECT count(created_by_email) FROM merchants WHERE created_by_email = ?1", nativeQuery = true)
	public int getByEmail(String createdByEmail);

	//檢查有沒有該店家名稱(計算數量>0)
	@Query(value = "SELECT count(name) FROM merchants WHERE name = ?1", nativeQuery = true)
	public int checkMerchantsName(String MerchantsName);

	@Query("SELECT m.name FROM Merchants m WHERE m.merchantsId = :merchantsId")
	String findNameByMerchantsId(@Param("merchantsId") int merchantsId);
	
	//取得該管理員的所有店家
	@Query(value = "SELECT * FROM merchants WHERE created_by_email = ?1", nativeQuery = true)
	public List<Merchants> getAllMerchantsByEmail(String createdByEmail);
	
	//取得所有管理員的所有店家
	@Query(value = "SELECT * FROM merchants WHERE Merchants_id = ?1", nativeQuery = true)
	public List<Merchants> getAllMerchantsByMerchantsId(int MerchantsId);
	
	//與該店家的距離
	@Query(value = "SELECT longitude_and_latitude FROM merchants WHERE Merchants_id = ?1", nativeQuery = true)
	Optional<String> getLongitudeAndLatitudeByMerchantsId(int MerchantsId);

	/**
     * 【全新的高效能查詢方法】
     * 使用原生 SQL 查詢，直接在資料庫中計算距離並篩選出範圍內的商家。
     * Haversine 公式被直接寫在 SQL 中。
     * @param userLat 使用者緯度
     * @param userLon 使用者經度
     * @param distance 範圍距離 (公里)
     * @return List<Merchants> 在範圍內的商家列表
     */
    @Query(value = "SELECT * FROM merchants m WHERE " +
                   "m.longitude_and_latitude IS NOT NULL AND " +
                   "(6371 * acos(cos(radians(:userLat)) * cos(radians(SUBSTRING_INDEX(m.longitude_and_latitude, ',', -1))) * " +
                   "cos(radians(SUBSTRING_INDEX(m.longitude_and_latitude, ',', 1)) - radians(:userLon)) + " +
                   "sin(radians(:userLat)) * sin(radians(SUBSTRING_INDEX(m.longitude_and_latitude, ',', -1))))) <= :distance",
           nativeQuery = true)
    List<Merchants> findMerchantsWithinRange(
        @Param("userLat") double userLat,
        @Param("userLon") double userLon,
        @Param("distance") double distance
    );
}
