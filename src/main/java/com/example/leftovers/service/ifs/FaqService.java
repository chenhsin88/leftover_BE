package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.FaqCreateReq;
import com.example.leftovers.vo.FaqDeleteReq;
import com.example.leftovers.vo.FaqGetByQuestionRes;
import com.example.leftovers.vo.FaqUpdateReq;

public interface FaqService {

	public BasicRes getAllFaq();
	
	public BasicRes createFaq(FaqCreateReq req);
	
	public FaqGetByQuestionRes getFaqByQuestion (String question);
	
	public BasicRes updateFaq(FaqUpdateReq req);
	
	public FaqGetByQuestionRes searchFaqsByKeyword(String keywords);
	
	public BasicRes deleteFaq(FaqDeleteReq req);
	
	
}
