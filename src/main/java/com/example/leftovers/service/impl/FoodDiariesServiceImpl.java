package com.example.leftovers.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.leftovers.constants.ResMessage;
import com.example.leftovers.dao.FoodDiariesDao;
import com.example.leftovers.entity.FoodDiaries;
import com.example.leftovers.service.ifs.FoodDiariesService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.FoodDiariesReq;

public class FoodDiariesServiceImpl implements FoodDiariesService{

	@Autowired
	private FoodDiariesDao foodDiariesDao;
	
	@Override
	public BasicRes create(FoodDiariesReq req) {
	    // 1. 驗證輸入
//	    if (req.getTitle() == null || req.getUserId() == null) {
//	        return new BasicRes(ResMessage.PARAM_ERROR.getCode(), "Title 或 UserId 不可為空");
//	    }

	    // 2. 新增資料
		FoodDiaries diary = new FoodDiaries();
        diary.setUserId(req.getUserId());
        diary.setTitle(req.getTitle());
        diary.setContentText(req.getContentText());
        diary.setImageUrl(req.getImageUrl());
        diary.setAssociatedMerchantId(req.getAssociatedMerchantId());
        diary.setVisibility(req.getVisibility());
        diary.setCreatedAt(LocalDateTime.now());
        diary.setUpdatedAt(LocalDateTime.now());
        
        foodDiariesDao.save(diary);
	    // 3. 回傳成功訊息
	    return new BasicRes(ResMessage.SUCCESS.getCode(), "新增成功");
	}

	@Override
	public BasicRes edit(FoodDiariesReq req) {
		 // 編輯通常是取得現有資料，用來前端顯示
	    Optional<FoodDiaries> diary = foodDiariesDao.findById(req.getUserId());
	    if (diary == null) {
	        return new BasicRes(ResMessage.NOT_FOUND.getCode(), "找不到該日記");
	    }
	    // 這邊可以設計回傳物件改成帶資料，但這裡先示範成功訊息
	    return new BasicRes(ResMessage.SUCCESS.getCode(), "取得成功");
	}

	@Override
	public BasicRes update(FoodDiariesReq req) {
	    // 先確認資料存在
	    Optional<FoodDiaries> foodDiaries = foodDiariesDao.findById(req.getUserId());
	    if (foodDiaries == null) {
	        return new BasicRes(ResMessage.NOT_FOUND.getCode(), "找不到該日記");
	    }
	    FoodDiaries diary = new FoodDiaries();
	    // 更新欄位
	    diary.setTitle(req.getTitle());
	    diary.setContentText(req.getContentText());
	    diary.setImageUrl(req.getImageUrl());
	    diary.setAssociatedMerchantId(req.getAssociatedMerchantId());
	    diary.setVisibility(req.getVisibility());
	    diary.setUpdatedAt(LocalDateTime.now());
	    
	    // 儲存更新
	    foodDiariesDao.save(diary);
	    return new BasicRes(ResMessage.SUCCESS.getCode(), "更新成功");
	}
	
	@Override
	public BasicRes del(FoodDiariesReq req) {
	    Optional<FoodDiaries> diary = foodDiariesDao.findById(req.getUserId());
	    if (diary == null) {
	        return new BasicRes(ResMessage.NOT_FOUND.getCode(), "找不到該日記");
	    }
	    // 刪除資料
	    foodDiariesDao.deleteById(req.getUserId());
	    return new BasicRes(ResMessage.SUCCESS.getCode(), "刪除成功");
	}

}
