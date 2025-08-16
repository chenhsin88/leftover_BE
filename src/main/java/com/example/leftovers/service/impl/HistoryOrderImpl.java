package com.example.leftovers.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.leftovers.dao.HistoryOrderDao;

import com.example.leftovers.entity.HistoryOrder;

import com.example.leftovers.service.ifs.HistoryOrderService;

import com.example.leftovers.vo.HistoryGetAllByMerchantIdRes;
import com.example.leftovers.vo.HistoryGetAllByMonthlyRevenueVo;
import com.example.leftovers.vo.HistoryGetAllByUserEmailRes;
import com.example.leftovers.vo.HistoryGetAllByYearRevenueVo;
import com.example.leftovers.vo.HistoryGetAllPriceByMerchantIdRes;
import com.example.leftovers.vo.HistoryGetAllPriceByMerchantIdVo;
import com.example.leftovers.vo.HistoryGetMerchantAllOrderVo;
import com.example.leftovers.vo.HistoryGetUserAllOrderVo;
import com.example.leftovers.vo.HistoryOrderGetByOrderIdRes;
import com.example.leftovers.vo.HistoryOrderItemVo;
import com.example.leftovers.vo.HistoryOrderVo;

@Service
public class HistoryOrderImpl implements HistoryOrderService {

	@Autowired
	private HistoryOrderDao historyorderDao;

	@Override
	public HistoryOrderGetByOrderIdRes HistoryOrderGetByOrderId(String orderId) {
		List<HistoryOrder> historyOrders = historyorderDao.findByOrderId(orderId);

		if (historyOrders.isEmpty()) {
			return new HistoryOrderGetByOrderIdRes(404, "查無歷史訂單資料", null);
		}

		List<HistoryOrderVo> voList = historyOrders.stream().map(h -> {
			HistoryOrderVo vo = new HistoryOrderVo();
			vo.setOrderId(h.getOrderId());
			vo.setFoodId(h.getFoodId());
			vo.setQuantity(h.getQuantity());
			vo.setUnitPrice(h.getUnitPrice());//總價錢
			vo.setFoodPrice(h.getFoodPrice());
			vo.setCreatedAt(h.getCreatedAt());
			vo.setFoodName(h.getFoodName());
			vo.setMerchantName(h.getMerchant());
			vo.setMerchantId(h.getMerchantId());
			vo.setUserEmail(h.getUserEmail());
			vo.setUserName(h.getUserName());
			vo.setPickupCode(h.getPickupCode());
			vo.setNotesToMerchant(h.getNotesToMerchant());
			vo.setPaymentMethodSimulated(h.getPaymentMethodSimulated());
			vo.setCancellationReason(h.getCancellationReason());
			vo.setRejectReason(h.getRejectReason());
			vo.setStatus(h.getStatus());
			return vo;
		}).toList();

		return new HistoryOrderGetByOrderIdRes(200, "查詢成功", voList);
	}

	@Override
	public HistoryGetAllByUserEmailRes HistoryGetAllByUserEmail(String userEmail) {
	    List<HistoryOrder> historyOrders = historyorderDao.findByUserEmail(userEmail);

	    // 依據 orderId 分組
	    Map<String, List<HistoryOrder>> groupedByOrderId = historyOrders.stream()
	        .collect(Collectors.groupingBy(HistoryOrder::getOrderId));

	    List<HistoryGetUserAllOrderVo> voList = new ArrayList<>();

	    for (Map.Entry<String, List<HistoryOrder>> entry : groupedByOrderId.entrySet()) {
	        String orderId = entry.getKey();
	        List<HistoryOrder> orderItems = entry.getValue();

	        // 初始化每一筆訂單資料
	        HistoryGetUserAllOrderVo vo = new HistoryGetUserAllOrderVo();
	        vo.setOrderId(orderId);
	        vo.setUserEmail(orderItems.get(0).getUserEmail());
	        vo.setCreatedAt(orderItems.get(0).getCreatedAt());
	        vo.setMerchantName(orderItems.get(0).getMerchant());
	        vo.setMerchantId(orderItems.get(0).getMerchantId());
	        vo.setUnitPrice(orderItems.get(0).getUnitPrice());
	        vo.setStatus(orderItems.get(0).getStatus());

//	        // 計算總價
//	        int totalUnitPrice = orderItems.stream()
//	            .mapToInt(item -> item.getFoodPrice() * item.getQuantity())
//	            .sum();
//	        vo.setUnitPrice(totalUnitPrice); // 設定訂單總價

	        // 每筆商品明細
	        List<HistoryOrderItemVo> itemVos = orderItems.stream().map(h -> {
	        	HistoryOrderItemVo item = new HistoryOrderItemVo();
	            item.setFoodName(h.getFoodName());
	            item.setFoodPrice(h.getFoodPrice());
	            item.setQuantity(h.getQuantity());
	            item.setFoodId(h.getFoodId());
	            return item;
	        }).collect(Collectors.toList());

	        vo.setItems(itemVos);
	        voList.add(vo);
	    }

	    return new HistoryGetAllByUserEmailRes(200, "查詢成功", voList);
	}

	@Override
	public HistoryGetAllByMerchantIdRes HistoryGetAllByMerchantId(int merchantId) {
		List<HistoryOrder> historyOrders = historyorderDao.findByMerchantId(merchantId);
		Set<String> seenOrderIds = new HashSet<>();
		// 只加入第一次看到的 orderId，重複的會被過濾掉
		List<HistoryGetMerchantAllOrderVo> voList = historyOrders.stream()
			.filter(h -> seenOrderIds.add(h.getOrderId())) 
			.map(h -> {
				HistoryGetMerchantAllOrderVo vo = new HistoryGetMerchantAllOrderVo();
				vo.setMerchantId(h.getMerchantId());
				vo.setCreatedAt(h.getCreatedAt());
				vo.setOrderId(h.getOrderId());
				vo.setUnitPrice(h.getUnitPrice());
				vo.setUserEmail(h.getUserEmail());
				vo.setUserName(h.getUserName());
				return vo;
			}).collect(Collectors.toList());
		for (HistoryGetMerchantAllOrderVo vo : voList) {
		    System.out.println("orderId: " + vo.getOrderId() +
		                       ", userName: " + vo.getUserName() +
		                       ", email: " + vo.getUserEmail() +
		                       ", unitPrice: " + vo.getUnitPrice() +
		                       ", createdAt: " + vo.getCreatedAt());
		}
		return new HistoryGetAllByMerchantIdRes(200, "查詢成功", voList);
	}

	// 計算營收
	@Override
	public HistoryGetAllPriceByMerchantIdRes HistoricalRevenue(int merchantId) {
		long count = historyorderDao.countByMerchantId(merchantId);
		if (count == 0) {
			return new HistoryGetAllPriceByMerchantIdRes(404, "查無歷史訂單資料", null);
		}

		// 取得今年年份
		int currentYear = LocalDate.now().getYear();

		// 只查今年月份資料
		List<Object[]> monthlyData = historyorderDao.findMonthlyRevenueByMerchantIdAndYear(merchantId, currentYear);

		// 年營收仍查全部
		List<Object[]> annualData = historyorderDao.findYearRevenueByMerchantId(merchantId);

		List<HistoryGetAllByMonthlyRevenueVo> monthlyRevenueList = new ArrayList<>();
		for (Object[] row : monthlyData) {
			String dateStr = (String) row[0]; // 格式像 "2025-06"
			String chineseMonth = convertToChineseMonth(dateStr);

			Long revenue = ((Number) row[1]).longValue();
			monthlyRevenueList.add(new HistoryGetAllByMonthlyRevenueVo(chineseMonth, revenue));
		}

		List<HistoryGetAllByYearRevenueVo> annualRevenueList = new ArrayList<>();
		for (Object[] row : annualData) {
			int year = ((Number) row[0]).intValue();
			Long revenue = ((Number) row[1]).longValue();
			annualRevenueList.add(new HistoryGetAllByYearRevenueVo(year, revenue));
		}

		HistoryGetAllPriceByMerchantIdVo vo = new HistoryGetAllPriceByMerchantIdVo();
		vo.setMerchantId(merchantId);
		vo.setMonthlyRevenue(monthlyRevenueList);
		vo.setYearRevenue(annualRevenueList);

		return new HistoryGetAllPriceByMerchantIdRes(200, "查詢成功", vo);
	}

	// 月份轉換中文格式
	private String convertToChineseMonth(String yyyyMM) {
		try {
			String[] parts = yyyyMM.split("-");
			int month = Integer.parseInt(parts[1]);
			return month + "月";
		} catch (Exception e) {
			return yyyyMM; // 如果格式錯誤就原樣返回
		}
	}

}
