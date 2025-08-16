package com.example.leftovers.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.leftovers.entity.FoodDiaries;

import jakarta.transaction.Transactional;

public interface FoodDiariesDao extends JpaRepository<FoodDiaries, Integer>{
	// 查詢某位使用者的日記
    List<FoodDiaries> findByUserId(int userId);

    // 查詢某筆日記是否存在
    @Query(value = "SELECT COUNT * FROM food_diaries WHERE id = ?1", nativeQuery = true)
    int existsByIdCustom(int id);

    // 用 @Modifying 來寫自定義的 update 或 delete
    @Modifying
    @Transactional
    @Query(value = "UPDATE food_diaries SET title = ?2, content_text = ?3 WHERE id = ?1", nativeQuery = true)
    int updateDiary(int id, String title, String contentText);
}
