package com.example.leftovers.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.leftovers.dao.FaqDao;
import com.example.leftovers.dao.UserDao;
import com.example.leftovers.entity.Faq;
import com.example.leftovers.service.ifs.FaqService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.FaqCreateReq;
import com.example.leftovers.vo.FaqDeleteReq;
import com.example.leftovers.vo.FaqVo;
import com.example.leftovers.vo.FaqGetByQuestionRes;
import com.example.leftovers.vo.FaqUpdateReq;

@Service
public class FaqImpl implements FaqService{

	 @Autowired
	    private FaqDao faqDao;
	 @Autowired
	 	private UserDao userDao;
	 
	 private boolean isAdmin(String email) {
		    return userDao.findByEmail(email)
		                  .map(user -> "Administrator".equals(user.getRole()))
		                  .orElse(false);
		}

	 @Override
	    public BasicRes getAllFaq() {
	        List<Faq> faqList = faqDao.findAll();
	        List<FaqVo> voList = faqList.stream()
	                .map(this::toFaqVo)
	                .collect(Collectors.toList());
	        return new FaqGetByQuestionRes(200, "查詢成功", voList);
	    }

	    @Override
	    public BasicRes createFaq(FaqCreateReq req) {
	    	if (!isAdmin(req.getAdminEmail())) {
	            return new BasicRes(403, "您沒有權限執行此操作");
	        }
	    	
	        if (faqDao.existsById(req.getQuestion())) {
	            return new BasicRes(400,"該問題已存在");
	        }

	        Faq faq = new Faq();
	        faq.setQuestion(req.getQuestion());
	        faq.setAnswer(req.getAnswer());
	        faq.setCategory(req.getCategory());
	        faq.setKeywords(req.getKeywords());
	        faq.setAdminId(req.getAdminId());
	        faq.setCreateAt(LocalDateTime.now());
	        faq.setUpdateAt(LocalDateTime.now());

	        faqDao.save(faq);
	        return new BasicRes(200,"建立成功");
	    }

	    @Override
	    public FaqGetByQuestionRes getFaqByQuestion(String question) {
	    	
	        List<Faq> result = faqDao.findByQuestionContainingIgnoreCase(question);
	        List<FaqVo> voList = result.stream()
	                .map(this::toFaqVo)
	                .collect(Collectors.toList());

	        return new FaqGetByQuestionRes(200, "查詢成功", voList);
	    }

	    @Override
	    public BasicRes updateFaq(FaqUpdateReq req) {
	    	
	    	if (!isAdmin(req.getAdminEmail())) {
	            return new BasicRes(403, "您沒有權限執行此操作");
	        }
	        Optional<Faq> optional = faqDao.findById(req.getQuestion());
	        if (!optional.isPresent()) {
	            return new BasicRes(404, "該問題不存在");
	        }

	        Faq faq = optional.get();
	        faq.setAnswer(req.getAnswer());
	        faq.setCategory(req.getCategory());
	        faq.setKeywords(req.getKeywords());
	        faq.setUpdateAt(LocalDateTime.now());

	        faqDao.save(faq);
	        return new BasicRes(200, "更新成功");
	    }
	    @Override
	    public FaqGetByQuestionRes searchFaqsByKeyword(String keyword) {
	        List<Faq> result = faqDao.findByKeywordsContainingIgnoreCase(keyword);

	        if (result.isEmpty()) {
	            return new FaqGetByQuestionRes(204, "查無符合的 FAQ 關鍵字", List.of());
	        }

	        List<FaqVo> voList = result.stream()
	                .map(this::toFaqVo)
	                .collect(Collectors.toList());

	        return new FaqGetByQuestionRes(200, "查詢成功", voList);
	    }

	    private FaqVo toFaqVo(Faq faq) {
	        FaqVo vo = new FaqVo();
	        vo.setQuestion(faq.getQuestion());
	        vo.setAnswer(faq.getAnswer());
	        vo.setCategory(faq.getCategory());
	        vo.setKeywords(faq.getKeywords());
	        vo.setAdminId(faq.getAdminId());
	        vo.setCreateAt(faq.getCreateAt());
	        vo.setUpdateAt(faq.getUpdateAt());
	        return vo;
	    }

	    @Override
	    public BasicRes deleteFaq(FaqDeleteReq req) {
	        // 1. 權限檢查
	    	 if (!isAdmin(req.getAdminEmail())) {
	    	        return new BasicRes(403, "您沒有權限執行此操作");
	    	    }

	    	 Optional<Faq> optional = faqDao.findById(req.getQuestion());
	    	    if (!optional.isPresent()) {
	    	        return new BasicRes(404, "該問題不存在");
	    	    }

	    	    faqDao.deleteById(req.getQuestion());

	    	    return new BasicRes(200, "刪除成功");
	    	}
	}