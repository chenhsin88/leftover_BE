package com.example.leftovers.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.leftovers.dao.CartsDao;
import com.example.leftovers.dao.FoodItemsDao;
import com.example.leftovers.dao.HistoryOrderDao;
import com.example.leftovers.dao.MerchantsDao;
import com.example.leftovers.dao.OrderDao;
import com.example.leftovers.dao.OrderItemDao;
import com.example.leftovers.dao.UserDao;
import com.example.leftovers.entity.FoodItems;
import com.example.leftovers.entity.HistoryOrder;
import com.example.leftovers.entity.OrderFoodItem;
import com.example.leftovers.entity.Orders;
import com.example.leftovers.entity.User;
import com.example.leftovers.exception.ResourceNotFoundException;
import com.example.leftovers.service.ifs.OrdersService;
import com.example.leftovers.service.ifs.SseService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.HistoryCreateReq;
import com.example.leftovers.vo.OrderCancellReq;
import com.example.leftovers.vo.OrderCreateRes;
import com.example.leftovers.vo.OrderFoodItemVo;
import com.example.leftovers.vo.OrderGetByMechantIdRes;
import com.example.leftovers.vo.OrderGetMerchantsVo;
import com.example.leftovers.vo.OrderItemReq;
import com.example.leftovers.vo.OrderPk;
import com.example.leftovers.vo.OrderRejectReq;
import com.example.leftovers.vo.OrderSimpleVo;
import com.example.leftovers.vo.OrderUpdateStatusReq;
import com.example.leftovers.vo.OrderVo;
import com.example.leftovers.vo.OrdersCreateReq;

import jakarta.transaction.Transactional;

@Service
public class OrderImpl implements OrdersService {

	@Autowired
	private MerchantsDao merchantsDao;

	@Autowired
	private HistoryOrderDao historyorderDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private CartsDao cartsdao;

	@Autowired
	private FoodItemsDao foodItemsDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SseService sseService;

	// 每天中午12點執行一次
	@Scheduled(cron = "0 0 12 * * ?")
	@Transactional
	public void cleanOldOrders() {
		try {
			LocalDateTime deadline = LocalDateTime.now().minusDays(1);

			// 先刪除明細再刪除主檔，避免 FK 錯誤
			List<OrderFoodItem> oldItems = orderItemDao.findByOrderedAtBefore(deadline);
			if (!oldItems.isEmpty()) {
				orderItemDao.deleteAllInBatch(oldItems); // 效能較佳
				System.out.println("已刪除 " + oldItems.size() + " 筆訂單明細");
			}

			List<Orders> oldOrders = orderDao.findByOrderedAtBefore(deadline);
			if (!oldOrders.isEmpty()) {
				orderDao.deleteAllInBatch(oldOrders); // 效能較佳
				System.out.println("已刪除 " + oldOrders.size() + " 筆訂單");
			}

		} catch (Exception e) {
			System.err.println("清除訂單時發生錯誤: " + e.getMessage());
		}
	}

	private long generateUniqueOrderId() {
		String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		// 計算查詢範圍：今天的最小和最大 ID
		long start = Long.parseLong(datePrefix + "0000");
		long end = Long.parseLong(datePrefix + "9999");

		// 查出今天已用的 orderId
		List<Long> todayOrders = orderDao.findByOrderIdBetween(start, end);

		// 取得尾碼（最後四位）
		Set<Integer> usedSuffixes = new HashSet<>();
		for (Long id : todayOrders) {
			int suffix = (int) (id % 10000);
			usedSuffixes.add(suffix);
		}

		// 建立候選 4 碼清單
		List<Integer> candidates = new ArrayList<>();
		for (int i = 1000; i <= 9999; i++) {
			if (!usedSuffixes.contains(i)) {
				candidates.add(i);
			}
		}

		// 若全滿，拋錯
		if (candidates.isEmpty()) {
			throw new RuntimeException("今天的 Order ID 已滿！");
		}

		// 隨機選一個沒用過的尾碼
		int randomSuffix = candidates.get(new Random().nextInt(candidates.size()));
		return Long.parseLong(datePrefix + randomSuffix);
	}

	// 產生不重複的 6 碼英數 pickupCode
	private String generateUniquePickupCode() {
		String pickupCode;
		do {
			pickupCode = randomAlphaNumeric(6);
		} while (orderDao.existsByPickupCode(pickupCode));
		return pickupCode;
	}

	// 產生 n 碼英數混合隨機字串
	private String randomAlphaNumeric(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	@Override
	@Transactional // 確保下面兩個表都寫入成功才 commit
	public BasicRes create(OrdersCreateReq req) {
		
		// 🔒 檢查使用者是否被停權或信譽太差
	    Optional<User> optionalUser = userDao.findByEmail(req.getUserEmail());
	    if (optionalUser.isEmpty()) {
	        return new BasicRes(400, "找不到使用者");
	    }
	    User user = optionalUser.get();

	    if (!user.isActive()) {
	        return new BasicRes(403, "帳號已被停權，無法下訂單");
	    }
	    if (user.getRating() >= 2) {
	        return new BasicRes(403, "信譽過低，無法下訂單");
	    }

		// 自動產生唯一的 orderId（6碼整數）
		long generatedOrderId = generateUniqueOrderId();

		// 自動產生唯一的 pickupCode（6碼英數）
		String generatedPickupCode = generateUniquePickupCode();

//		
//		if (req.getPickupConfirmedAt() != null && req.getPickupConfirmedAt().isBefore(LocalDateTime.now())) {
//		    return new BasicRes(400, "取餐時間不能早於現在時間");
//		}
		for (OrderItemReq item : req.getOrderItems()) {

			if (item.getQuantity() <= 0 || item.getFoodPrice() < 0) {
				return new BasicRes(400, "餐點數量與金額必須為正值");
			}
		}

		Orders order = new Orders();
		order.setOrderId(generatedOrderId);
		order.setUserName(req.getUserName());
		order.setMerchantsId(req.getMerchantId());
		order.setPickupCode(generatedPickupCode);
		order.setTotalAmount(req.getTotalAmount());
//		order.setOriginalTotalAmount(req.getOriginalTotalAmount());
		order.setStatus(req.getStatus());
		order.setPaymentMethodSimulated(req.getPaymentMethodSimulated());
		order.setNotesToMerchant(req.getNotesToMerchant());
		order.setUserEmail(req.getUserEmail());
//		order.setPickupConfirmedAt(req.getPickupConfirmedAt());
		order.setMerchant(req.getMerchant());

		orderDao.save(order); // 寫入 orders table
		
		 // 這邊寫推播通知
		// ================== 【關鍵修正 - 無 Logger 版本】開始 ==================
		// 判斷支付方式，只有在為「現金」時，才在訂單建立的當下立即發送通知。
		// 信用卡訂單的通知，將由 PaymentController 在確認付款成功後發送。
		if ("CASH".equalsIgnoreCase(req.getPaymentMethodSimulated())) {

		    // 1. 準備要發送給前端的通知內容 (payload)
		    Map<String, Object> notificationData = Map.of(
		        "orderId", generatedOrderId,
		        "userName", req.getUserName(),
		        "totalAmount", req.getTotalAmount(),
		        "orderAt", LocalDateTime.now().toString(),
		        "status", req.getStatus(),
		        "pickupCode", generatedPickupCode
		    );

		    // 2. 呼叫 SSE 服務，將通知內容發送給指定的商家
		    sseService.pushMessageToMerchant(req.getMerchantId(), notificationData);

		    // 3. 使用 System.out.println 記錄系統事件
		    System.out.println("現金訂單 " + generatedOrderId + " 已成功建立，並已發送 SSE 通知給商家 ID: " + req.getMerchantId());

		} else {
		    // 如果是信用卡等其他支付方式，則只記錄資訊，不發送通知
		    System.out.println("信用卡訂單 " + generatedOrderId + " 已初步建立，等待付款確認，此階段不發送 SSE 通知。");
		}
		// ================== 【關鍵修正】結束 ==================

		for (OrderItemReq item : req.getOrderItems()) {

			// 從特定商家查找指定 foodId 的商品
			FoodItems dbFoodItem = foodItemsDao.findByFoodIdAndMerchant(item.getFoodId(), req.getMerchantId())
					.orElseThrow(() -> new RuntimeException("找不到該商家的商品，foodId: " + item.getFoodId()));

			// 確保庫存夠
			if (dbFoodItem.getQuantityAvailable() < item.getQuantity()) {
//				throw new RuntimeException("商品庫存不足：" + dbFoodItem.getName());
				return new BasicRes(400, "商品庫存不足");
			}

			OrderFoodItem foodItem = new OrderFoodItem();
			foodItem.setOrderId(generatedOrderId); // 統一用主訂單的 orderId
			foodItem.setMerchantsId(req.getMerchantId()); // 統一用主訂單的 merchantId
			foodItem.setFoodId(item.getFoodId());
			foodItem.setQuantity(item.getQuantity());
			foodItem.setFoodName(item.getFoodName());
			foodItem.setFoodPrice(dbFoodItem.getDiscountedPrice());
			foodItem.setMerchant(req.getMerchant());

			orderItemDao.save(foodItem); // 寫入 order_food_item table

			// 扣庫存
			dbFoodItem.setQuantityAvailable(dbFoodItem.getQuantityAvailable() - item.getQuantity());
			foodItemsDao.save(dbFoodItem); // 更新庫存
		}
		List<Integer> foodItemIds = req.getOrderItems().stream().map(OrderItemReq::getFoodId) // 如果 DTO 還叫 foodId 是 OK 的
				.collect(Collectors.toList());

		cartsdao.deleteMultipleItems(req.getUserEmail(), req.getMerchantId(), foodItemIds);

		return new OrderCreateRes(200, "訂單建立成功", generatedOrderId);
	}

	@Override
	public BasicRes requestCancel(OrderCancellReq req) {
		OrderPk pk = new OrderPk(req.getOrderId(), req.getPickupCode());

		Optional<Orders> optionalOrder = orderDao.findById(pk);
		if (optionalOrder.isEmpty()) {
			return new BasicRes(400, "查無此訂單");
		}

		Orders order = optionalOrder.get();

		LocalDateTime orderTime = order.getOrderedAt();
		if (orderTime.plusMinutes(20).isBefore(LocalDateTime.now())) {
			return new BasicRes(400, "訂單已成立超過 20 分鐘，無法取消");
		}

		if ("reject".equalsIgnoreCase(order.getCancelStatus())) {
			return new BasicRes(400, "訂單已駁回，無法再次申請取消");
		}
		if ("finish".equalsIgnoreCase(order.getCancelStatus())) {
			return new BasicRes(400, "訂單已完成，無法取消");
		}
		if ("PENDING".equalsIgnoreCase(order.getCancelStatus())) {
			return new BasicRes(400, "取消申請已在審核中，請勿重複提交");
		}

		order.setCancelStatus("PENDING"); // 設定狀態為待審核
		order.setStatus("cancelled_by_user");
		order.setCancellationReason(req.getCancellationReason()); // 設定取消原因
		orderDao.save(order);

		return new BasicRes(200, "取消申請已送出，請等待審核");
	}

	@Override
	public BasicRes cancell(OrderCancellReq req) {
		OrderPk pk = new OrderPk(req.getOrderId(), req.getPickupCode());
		Optional<Orders> optional = orderDao.findById(pk);
		if (optional.isEmpty()) {
			return new BasicRes(400, "查無此訂單");
		}

		Orders order = optional.get();

		// 查是否已有歷史紀錄
		String historyOrderId = "RC" + order.getOrderId();
		if (historyorderDao.existsByOrderId(historyOrderId)) {
			return new BasicRes(400, "該訂單已建立過歷史紀錄，請勿重複操作");
		}

		// 更新狀態
		order.setStatus("cancelled_by_merchant");
		order.setCancelStatus("ALLOW");
		orderDao.save(order);

		// 建立歷史紀錄
		List<OrderFoodItem> itemEntities = orderItemDao.findByOrderId(order.getOrderId());
		for (OrderFoodItem foodItem : itemEntities) {
			// 返還庫存
			FoodItems dbFoodItem = foodItemsDao.findByFoodIdAndMerchant(foodItem.getFoodId(), order.getMerchantsId())
					.orElseThrow(() -> new RuntimeException("找不到商品，無法返還庫存"));

			dbFoodItem.setQuantityAvailable(dbFoodItem.getQuantityAvailable() + foodItem.getQuantity());
			foodItemsDao.save(dbFoodItem); // 更新回 DB

			HistoryCreateReq historyReq = new HistoryCreateReq();
			historyReq.setOrderId(historyOrderId);
			historyReq.setFoodId(foodItem.getFoodId());
			historyReq.setQuantity(foodItem.getQuantity());
			historyReq.setUnitPrice(order.getTotalAmount());
			historyReq.setFoodPrice(foodItem.getFoodPrice());
			historyReq.setCreatedAt(order.getOrderedAt());
			historyReq.setFoodName(foodItem.getFoodName());
			historyReq.setMerchant(foodItem.getMerchant());
			historyReq.setMerchantId(order.getMerchantsId());
			historyReq.setUserEmail(order.getUserEmail());
			historyReq.setUserName(order.getUserName());
			historyReq.setPickupCode(order.getPickupCode());
			historyReq.setNotesToMerchant(order.getNotesToMerchant());
			historyReq.setPaymentMethodSimulated(order.getPaymentMethodSimulated());
			historyReq.setCancellationReason(order.getCancellationReason());
			historyReq.setRejectReason(order.getRejectReason());
			historyReq.setStatus(order.getStatus());

			HistoryOrderCreate(historyReq);
		}

		return new BasicRes(200, "訂單已取消，並建立歷史紀錄");
	}

	@Override
	public OrderVo getOrderInformationByOrderId(long orderId) {
		// 查出主訂單
		Orders orderEntity = orderDao.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("訂單不存在"));

		// 查出對應的訂單餐點項目
		List<OrderFoodItem> itemEntities = orderItemDao.findByOrderId(orderId);

		// 將 entity 轉換為 VO
		OrderVo orderVo = new OrderVo();
		orderVo.setOrderId(orderEntity.getOrderId());
		orderVo.setUserName(orderEntity.getUserName());
		orderVo.setStatus(orderEntity.getStatus());
		orderVo.setTotalAmount(orderEntity.getTotalAmount());
		orderVo.setOrderedAt(orderEntity.getOrderedAt());
		orderVo.setPickupCode(orderEntity.getPickupCode());
		orderVo.setNotesToMerchant(orderEntity.getNotesToMerchant());
		orderVo.setPaymentMethodSimulated(orderEntity.getPaymentMethodSimulated());
		// 取消
		orderVo.setCancelStatus(orderEntity.getCancelStatus());
		orderVo.setCancellationReason(orderEntity.getCancellationReason());
		orderVo.setRejectReason(orderEntity.getRejectReason());

		// 餐點轉換
		List<OrderFoodItemVo> itemVos = itemEntities.stream().map(entity -> {
			OrderFoodItemVo vo = new OrderFoodItemVo();
			vo.setMerchantsId(entity.getMerchantsId());
			vo.setFoodId(entity.getFoodId());
			vo.setOrderId(entity.getOrderId());
			vo.setQuantity(entity.getQuantity());
			vo.setFoodName(entity.getFoodName());
			vo.setFoodPrice(entity.getFoodPrice());
			return vo;
		}).toList();

		orderVo.setOrderFoodItemList(itemVos);

		return orderVo;
	}

	@Override
	public BasicRes updateStatus(OrderUpdateStatusReq req) {
		int updatedCount = orderDao.updateStatusByIdAndPickupCode(req.getOrderId(), req.getPickupCode(),
				req.getStatus());

//		boolean hasHistory = historyorderDao.existsById(req.getOrderId());
		// 正確的程式碼
		String historyOrderId = "RF" + req.getOrderId();
		boolean hasHistory = historyorderDao.existsByOrderId(historyOrderId);

		if (hasHistory) {
			return new BasicRes(400, "該訂單已建立過歷史紀錄，請勿重複操作");
		}
		if (updatedCount == 1) {
			Optional<Orders> optionalOrder = orderDao.findByOrderId(req.getOrderId());
			List<OrderFoodItem> itemEntities = orderItemDao.findByOrderId(req.getOrderId());

			if (optionalOrder.isPresent() && !itemEntities.isEmpty()) {

				Orders order = optionalOrder.get();

				// 只有當訂單狀態為 finish 時才建立歷史紀錄
				if ("completed".equalsIgnoreCase(order.getStatus())
						|| "cancelled_by_merchant".equalsIgnoreCase(order.getStatus())) {
					for (OrderFoodItem foodItem : itemEntities) {
						HistoryCreateReq historyReq = new HistoryCreateReq();
						historyReq.setOrderId("RF" + order.getOrderId());
						historyReq.setFoodId(foodItem.getFoodId());
						historyReq.setQuantity(foodItem.getQuantity());
						historyReq.setUnitPrice(order.getTotalAmount());
						historyReq.setFoodPrice(foodItem.getFoodPrice());
						historyReq.setCreatedAt(order.getOrderedAt());
						historyReq.setFoodName(foodItem.getFoodName());
						historyReq.setMerchant(foodItem.getMerchant());
						historyReq.setMerchantId(order.getMerchantsId());
						historyReq.setUserEmail(order.getUserEmail());
						historyReq.setUserName(order.getUserName());
						historyReq.setPickupCode(order.getPickupCode());
						historyReq.setNotesToMerchant(order.getNotesToMerchant());
						historyReq.setPaymentMethodSimulated(order.getPaymentMethodSimulated());
						historyReq.setCancellationReason(order.getCancellationReason());
						historyReq.setRejectReason(order.getRejectReason());
						historyReq.setStatus(order.getStatus());

						HistoryOrderCreate(historyReq);
					}
				} else {
					return new BasicRes(400, "訂單尚未完成，無法建立歷史紀錄");
				}
			}

			return new BasicRes(200, "訂單狀態更新成功");
		} else {
			return new BasicRes(400, "找不到訂單或更新失敗");
		}
	}

	private void HistoryOrderCreate(HistoryCreateReq req) {

		HistoryOrder historyorder = new HistoryOrder();
		historyorder.setOrderId(req.getOrderId());
		historyorder.setFoodId(req.getFoodId());
		historyorder.setQuantity(req.getQuantity());
		historyorder.setUnitPrice(req.getUnitPrice());
		historyorder.setFoodPrice(req.getFoodPrice());
		historyorder.setCreatedAt(req.getCreatedAt());
		historyorder.setFoodName(req.getFoodName());
		historyorder.setMerchant(req.getMerchant());
		historyorder.setMerchantId(req.getMerchantId());
		historyorder.setUserEmail(req.getUserEmail());
		historyorder.setUserName(req.getUserName());
		historyorder.setPickupCode(req.getPickupCode());
		historyorder.setNotesToMerchant(req.getNotesToMerchant());
		historyorder.setStatus(req.getStatus());
		historyorder.setPaymentMethodSimulated(req.getPaymentMethodSimulated());
		historyorder.setCancellationReason(req.getCancellationReason());
		historyorder.setRejectReason(req.getRejectReason());

		historyorderDao.save(historyorder);
	}

	@Override
	public OrderGetByMechantIdRes orderGetByMechantId(int merchantId) {

		List<Orders> orders = orderDao.findOrdersByMerchantsId(merchantId);
		 // 過濾掉狀態為 "finish" 或 "cancelled_by_merchant" 的訂單（不分大小寫）
	    List<OrderSimpleVo> simpleVos = orders.stream()
	        .filter(order -> {
	            String status = order.getStatus();
	            return status != null 
	                && !"finish".equalsIgnoreCase(status)
	                && !"cancelled_by_merchant".equalsIgnoreCase(status);
	        })
	        .map(order -> new OrderSimpleVo(
	            order.getOrderId(), 
	            order.getUserName(), 
	            order.getTotalAmount(),
	            order.getOrderedAt(), 
	            order.getStatus(), 
	            order.getPickupCode()))
	        .toList();

	    return new OrderGetByMechantIdRes(simpleVos);
	}

	@Override
	@Transactional
	public BasicRes reject(OrderRejectReq req) {
		// 查詢訂單是否存在
		Orders order = orderDao.findByOrderId(req.getOrderId()).orElseThrow(() -> new RuntimeException("找不到訂單"));

		// 若訂單狀態不是 pending ，不能駁回
		if (!"PENDING".equalsIgnoreCase(order.getCancelStatus())) {
			return new BasicRes(400, "此訂單無法駁回");
		}

		// 設定駁回狀態與理由
		order.setCancelStatus("REJECT");
		order.setRejectReason(req.getRejectReason());
		// 根據付款方式設定不同的狀態
		String paymentMethod = order.getPaymentMethodSimulated();
		if ("CASH".equalsIgnoreCase(paymentMethod)) {
			order.setStatus("PENDING");
		} else if ("CREDIT_CARD".equalsIgnoreCase(paymentMethod)) {
			order.setStatus("PICKED_UP");
		}
		// 儲存變更
		orderDao.save(order);

		return new BasicRes(200, "訂單已駁回");
	}

	@Override
	public List<OrderVo> getOrderInformationByEmail(String userEmail) {
		List<Orders> orders = orderDao.findAllByUserEmail(userEmail);

		if (orders.isEmpty()) {
		    throw new ResourceNotFoundException("找不到該使用者的訂單");
		}

		return orders.stream().map(order -> {
			long orderId = order.getOrderId();
			List<OrderFoodItem> itemEntities = orderItemDao.findByOrderId(orderId);

			List<OrderFoodItemVo> itemVos = itemEntities.stream().map(item -> {
				OrderFoodItemVo vo = new OrderFoodItemVo();
				vo.setMerchantsId(item.getMerchantsId());
				vo.setFoodId(item.getFoodId());
				vo.setOrderId(item.getOrderId());
				vo.setQuantity(item.getQuantity());
				vo.setFoodName(item.getFoodName());
				vo.setFoodPrice(item.getFoodPrice());
				return vo;
			}).toList();
			// 2. 取得商家資訊（假設一張訂單只對應一個商家）
			// 從第一筆 food item 取 merchantId
			OrderGetMerchantsVo merchantVo = new OrderGetMerchantsVo();
			if (!itemEntities.isEmpty()) {
				int merchantId = itemEntities.get(0).getMerchantsId();
				merchantsDao.findById(merchantId).ifPresent(merchant -> {
					merchantVo.setAddressText(merchant.getAddressText());
					merchantVo.setName(merchant.getName());
					merchantVo.setPhoneNumber(merchant.getPhoneNumber());
					merchantVo.setOpeningHoursDescription(merchant.getOpening_hoursDescription());
				});
			}

			OrderVo vo = new OrderVo();
			vo.setOrderId(order.getOrderId());
			vo.setUserName(order.getUserName());
			vo.setTotalAmount(order.getTotalAmount());
			vo.setStatus(order.getStatus());
			vo.setPaymentMethodSimulated(order.getPaymentMethodSimulated());
			vo.setPickupCode(order.getPickupCode());
			vo.setNotesToMerchant(order.getNotesToMerchant());
			vo.setOrderedAt(order.getOrderedAt());
			vo.setRejectReason(order.getRejectReason());
			vo.setCancellationReason(order.getCancellationReason());
			vo.setCancelStatus(order.getCancelStatus());
			vo.setOrderFoodItemList(itemVos);
			vo.setMerchantInfo(merchantVo);
			return vo;
		}).toList();
	}

	@Override
	@Transactional
	public BasicRes notToken(long orderId) {
		Optional<Orders> optionalOrder = orderDao.findByOrderId(orderId);
		if (optionalOrder.isEmpty()) {
			return new BasicRes(400, "找不到訂單");
		}

		Orders order = optionalOrder.get();

		LocalDateTime now = LocalDateTime.now();
		boolean isOverOneHour = order.getPickupConfirmedAt() == null
				&& order.getOrderedAt().plusHours(1).isBefore(now);

		if (!isOverOneHour) {
			return new BasicRes(400, "尚未超過一小時，不符合未取餐處理條件");
		}

		// 狀態改為 not_token
		order.setStatus("not_token");
		orderDao.save(order);

		// 使用者信譽 +1
		userDao.increaseUserRating(order.getUserEmail());


	    // ✅ 判斷是否 >=2 就停權
	    Optional<User> userOptional = userDao.findByEmail(order.getUserEmail());
	    if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        if (user.getRating() >= 2) {
	            user.setActive(false);
	            userDao.save(user);
	        }
	    }
	    
	 // ✅ 檢查是否已寫入歷史紀錄
	    String historyOrderIdRF = "RF" + order.getOrderId();
	    String historyOrderIdRC = "RC" + order.getOrderId();
	    String historyOrderIdRN = "RN" + order.getOrderId();

	    boolean hasHistory = historyorderDao.existsByOrderId(historyOrderIdRF) || 
	                         historyorderDao.existsByOrderId(historyOrderIdRC) ||
	                         historyorderDao.existsByOrderId(historyOrderIdRN);

		if (!hasHistory) {
			List<OrderFoodItem> itemEntities = orderItemDao.findByOrderId(order.getOrderId());
			if (!itemEntities.isEmpty()) {
				for (OrderFoodItem foodItem : itemEntities) {
					HistoryCreateReq historyReq = new HistoryCreateReq();
					historyReq.setOrderId("RN" + order.getOrderId());
					historyReq.setFoodId(foodItem.getFoodId());
					historyReq.setQuantity(foodItem.getQuantity());
					historyReq.setUnitPrice(order.getTotalAmount());
					historyReq.setFoodPrice(foodItem.getFoodPrice());
					historyReq.setCreatedAt(order.getOrderedAt());
					historyReq.setFoodName(foodItem.getFoodName());
					historyReq.setMerchant(foodItem.getMerchant());
					historyReq.setMerchantId(order.getMerchantsId());
					historyReq.setUserEmail(order.getUserEmail());
					historyReq.setUserName(order.getUserName());
					historyReq.setPickupCode(order.getPickupCode());
					historyReq.setNotesToMerchant(order.getNotesToMerchant());
					historyReq.setPaymentMethodSimulated(order.getPaymentMethodSimulated());
					historyReq.setCancellationReason(order.getCancellationReason());
					historyReq.setRejectReason(order.getRejectReason());
					historyReq.setStatus(order.getStatus());

					HistoryOrderCreate(historyReq);
				}
			}
		}

		return new BasicRes(200, "已標記為未取餐，使用者信譽加一");
	}


}
