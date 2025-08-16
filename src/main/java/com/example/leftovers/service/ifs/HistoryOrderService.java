package com.example.leftovers.service.ifs;


//import com.example.leftovers.vo.BasicRes;
//import com.example.leftovers.vo.HistoryCreateReq;
import com.example.leftovers.vo.HistoryGetAllByMerchantIdRes;
import com.example.leftovers.vo.HistoryGetAllByUserEmailRes;
import com.example.leftovers.vo.HistoryGetAllPriceByMerchantIdRes;
import com.example.leftovers.vo.HistoryOrderGetByOrderIdRes;

public interface HistoryOrderService {

//	public BasicRes HistoryOrderCreate(HistoryCreateReq req);//建立歷史訂單
	
	public HistoryOrderGetByOrderIdRes HistoryOrderGetByOrderId(String orderId);//取單單筆訂單詳細資訊
	
	public HistoryGetAllByUserEmailRes HistoryGetAllByUserEmail(String userEmail);//取得使用者所有訂單
	
	public HistoryGetAllByMerchantIdRes HistoryGetAllByMerchantId(int merchantId);
	
	public HistoryGetAllPriceByMerchantIdRes HistoricalRevenue(int merchantId);//年度收入

}
