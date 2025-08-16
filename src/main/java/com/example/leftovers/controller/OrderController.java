package com.example.leftovers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.leftovers.service.ifs.OrdersService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.OrderCancellReq;
import com.example.leftovers.vo.OrderGetByMechantIdRes;
import com.example.leftovers.vo.OrderRejectReq;
import com.example.leftovers.vo.OrderUpdateStatusReq;
import com.example.leftovers.vo.OrderVo;
import com.example.leftovers.vo.OrdersCreateReq;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:8080")
public class OrderController {

	@Autowired
    private OrdersService ordersService;
	

    @PostMapping("/create")
    public ResponseEntity<BasicRes> createOrder(@Valid @RequestBody OrdersCreateReq req) {
        BasicRes response = ordersService.create(req);
        return ResponseEntity.ok(response);
    }
   

    /**
     * 根據 merchantId 查詢所有訂單（簡易資訊）
     */
    @GetMapping("/getAllOrder/{merchantsId}")
    public ResponseEntity<OrderGetByMechantIdRes> getOrdersByMerchantId(@PathVariable("merchantsId") int merchantsId) {
        OrderGetByMechantIdRes res = ordersService.orderGetByMechantId(merchantsId);
        return ResponseEntity.ok(res);
    }
    
    /**
     * 根據訂單 ID 查詢訂單詳細資訊（含商品列表）
     */
    @GetMapping("/getOrderInformation/{orderId}")
    public ResponseEntity<OrderVo> getOrderById(@PathVariable("orderId") long orderId) {
        OrderVo vo = ordersService.getOrderInformationByOrderId(orderId);
        return ResponseEntity.ok(vo);
    }
    /**
     * 前台 - 送出取消申請（帶取消原因）
     */
    @PostMapping("/cancelRequest")
    public BasicRes requestCancel(@Valid @RequestBody OrderCancellReq req) {
        return ordersService.requestCancel(req);
    }

    /**
     * 後台 - 審核取消訂單，通過後真正刪除
     */
    @PostMapping("/cancell")
    public BasicRes cancell(@Valid @RequestBody OrderCancellReq req) {
        return ordersService.cancell(req);
    }
    
    @PostMapping("/updateStatus")
    public BasicRes updateStatus(@Valid @RequestBody OrderUpdateStatusReq req) {
        return ordersService.updateStatus(req);
    }
    
    // 商家駁回訂單
    @PostMapping("/reject")
    public ResponseEntity<BasicRes> rejectOrder(@RequestBody OrderRejectReq req) {
        try {
            BasicRes res = ordersService.reject(req);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BasicRes(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BasicRes(500, "伺服器錯誤"));
        }
    }
    
    
    //格式http://localhost:8080/orders/getOrderInformationByEmail?userEmail=(填入信箱)
    @GetMapping("/getOrderInformationByEmail")
    public ResponseEntity<List<OrderVo>> getOrdersByEmail(@RequestParam("userEmail") String userEmail) {
        List<OrderVo> orders = ordersService.getOrderInformationByEmail(userEmail);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/notToken")
    public BasicRes NotToken(@RequestParam("orderId") long orderId) {
        return ordersService.notToken(orderId);
    }
   
}
