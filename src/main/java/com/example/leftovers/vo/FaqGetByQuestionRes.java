package com.example.leftovers.vo;

import java.util.List;

public class FaqGetByQuestionRes extends BasicRes{

	  private List<FaqVo> faqList;
	  
	  public FaqGetByQuestionRes() {
	        super();
	    }

	  public FaqGetByQuestionRes(int code, String message, List<FaqVo> faqList) {
	        super(code, message);
	        this.faqList = faqList;
	    }

	public List<FaqVo> getFaqList() {
		return faqList;
	}

	public void setFaqList(List<FaqVo> faqList) {
		this.faqList = faqList;
	}
	  
}
