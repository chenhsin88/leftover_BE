package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.FoodDiariesReq;

public interface FoodDiariesService {

	public BasicRes create(FoodDiariesReq Req);
	
	public BasicRes edit(FoodDiariesReq Req);
	
	public BasicRes update(FoodDiariesReq Req);
	
	public BasicRes del(FoodDiariesReq Req);
	

}
