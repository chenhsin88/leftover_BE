package com.example.leftovers.controller;

import com.example.leftovers.service.ifs.FaqService;
import com.example.leftovers.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    // 1. 查詢所有 FAQ
    @GetMapping("/all")
    public BasicRes getAllFaq() {
        return faqService.getAllFaq();
    }

    // 2. 建立 FAQ
    @PostMapping("/create")
    public BasicRes createFaq(@RequestBody @Valid FaqCreateReq req) {
        return faqService.createFaq(req);
    }

    // 3. 根據問題查詢 FAQ（模糊搜尋）
    @GetMapping("/search")
    public FaqGetByQuestionRes getFaqByQuestion(@RequestParam(name = "question") String question) {
        return faqService.getFaqByQuestion(question);
    }

    // 4. 更新 FAQ（根據 question 找出並更新其他欄位）
    @PostMapping("/update")
    public BasicRes updateFaq(@RequestBody @Valid FaqUpdateReq req) {
        return faqService.updateFaq(req);
    }

    // 5. 根據關鍵字搜尋 FAQ（模糊搜尋）
    @GetMapping("/search/keyword")
    public FaqGetByQuestionRes searchByKeyword(@RequestParam("keyword") String keyword) {
        return faqService.searchFaqsByKeyword(keyword);
    }
    //6.刪除
    @PostMapping("/delete")
    public BasicRes deleteReq(@RequestBody @Valid FaqDeleteReq req) {
        return faqService.deleteFaq(req);
    }
}
