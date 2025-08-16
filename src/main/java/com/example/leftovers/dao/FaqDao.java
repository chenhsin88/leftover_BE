package com.example.leftovers.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.leftovers.entity.Faq;


public interface FaqDao extends JpaRepository<Faq, String>{
	List<Faq> findByQuestionContainingIgnoreCase(String keyword);

    List<Faq> findByCategory(String category);

    List<Faq> findByKeywordsContainingIgnoreCase(String keyword);
	
}
