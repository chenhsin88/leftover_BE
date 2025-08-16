package com.example.leftovers.service.ifs;

import java.util.List;

import com.example.leftovers.vo.BasicRes;

import com.example.leftovers.vo.OrderCancellReq;

import com.example.leftovers.vo.OrderGetByMechantIdRes;
import com.example.leftovers.vo.OrderRejectReq;
import com.example.leftovers.vo.OrderUpdateStatusReq;
import com.example.leftovers.vo.OrderVo;
import com.example.leftovers.vo.OrdersCreateReq;

public interface OrdersService {

	//使用者
	public BasicRes create(OrdersCreateReq req);//建立訂單
	
	public BasicRes requestCancel(OrderCancellReq req);//前台確認申請
	
	public List<OrderVo> getOrderInformationByEmail(String email);
	
	//共用
	public OrderVo  getOrderInformationByOrderId(long orderId);//透過ID取得相對訂單資料
	
	
	
	//商家
	public BasicRes updateStatus(OrderUpdateStatusReq req);//更新狀態(status)
	
	public OrderGetByMechantIdRes orderGetByMechantId(int merchantsId);//透過商家名稱篩選各自訂單

	public BasicRes cancell(OrderCancellReq req);//後台確認刪除 同意取消訂單
	
	public BasicRes reject(OrderRejectReq req);//商家駁回
	
	public BasicRes notToken(long orderId);//未取餐
	
}
