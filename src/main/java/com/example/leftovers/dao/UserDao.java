package com.example.leftovers.dao;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.leftovers.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, String> {

	// 查詢用（選擇性）
    boolean existsByEmail(String email); // 可檢查帳號是否存在

    Optional<User> findByEmail(String email); // 登入時查詢用

    // 查詢信箱是否存在
    @Query(value = "SELECT email FROM users WHERE email = ?1", nativeQuery = true)
    String checkEmail(String email);

 // 查詢信箱是否存在
    @Query(value = "SELECT role FROM users WHERE email = ?1", nativeQuery = true)
    String getRole(String email);
    
    @Query(value = "SELECT regular_registration FROM users WHERE email = ?1", nativeQuery = true)
    Byte getRegularRegistration(String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.rating = COALESCE(u.rating, 0) + 1 WHERE u.email = :email")
    void increaseUserRating(@Param("email") String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = false WHERE u.email = :email")
    void deactivateUser(@Param("email") String email);
}