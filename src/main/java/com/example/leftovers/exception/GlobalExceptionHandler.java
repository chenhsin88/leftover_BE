package com.example.leftovers.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.access.AccessDeniedException;
/**
 * 1. @RestControllerAdvice 包含了 @ControllerAdvice 和 @ResponseBody <br>
 * 1.1. @ControllerAdvice: 使用於 class 的註釋，用來定義「全域」的例外處理策略<br>
 * 1.2. @ResponseBody: 這是一個告訴 Spring 每次 response 都需要將返回的物件序列化為 JSON 或 XML 格式，
 * 然後放入 HTTP 回應的內容中<br>
 * 2. @ExceptionHandler: 使用於 method 中的註釋，提供了在 controller 處理例外的功能<br>
 * 3. ResponseEntity: 可以做為 controller 的返回值
 */
@RestControllerAdvice // 用來表示這是一個全域的 REST 例外處理器
public class GlobalExceptionHandler {


	//專門處理登入失敗，回傳 401
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationFailed(AuthenticationFailedException e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("code", HttpStatus.UNAUTHORIZED.value()); 
        errorMap.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
    }
    
    // 通用處理器，現在可以將它修改為回傳 500
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> errorMap = new HashMap<>();
        // 對於未預期的錯誤，回傳 500 更合適
        errorMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value()); 
        errorMap.put("message", "伺服器發生未預期的錯誤");
        e.printStackTrace(); // 在後台印出詳細錯誤，方便除錯
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
    }
	
	
	// @Valid 配合 Spring 會抛出 MethodArgumentNotValidException 異常；所以要針對此 Exception 處理
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Map<String, Object>> paramExceptionHandler(MethodArgumentNotValidException e) {
		// 因為在 controller 中的 res 返回的是 code: 數值 和 message: 字串；所以 map 的 value 用 Object 接
		Map<String, Object> errorMap = new HashMap<>();
		// 將 req 中的參數檢查統一歸類為 bad_request，代碼為 400
		errorMap.put("code", HttpStatus.BAD_REQUEST.value());
		// 即使有多個錯誤參數，但還是一個個處理，所以會取第一個錯誤訊息
		errorMap.put("message", e.getAllErrors().get(0).getDefaultMessage());
		// 返回的 Http Status Code 為 badRequest(400)
		return ResponseEntity.badRequest().body(errorMap);
	}

	@ExceptionHandler({ SQLException.class})
	public ResponseEntity<Map<String, Object>> handleSQLException(SQLException e) {// 抓取 SQL Exception
		// 因為在 controller 中的 res 返回的是 code: 數值 和 message: 字串；所以 map 的 value 用 Object 接
		Map<String, Object> errorMap = new HashMap<>();
		// 將 SQL 發生的 Exception 歸類為 internal_server_error，代碼為 500
		errorMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		// 即使有多個錯誤參數，但還是一個個處理，所以會取第一個錯誤訊息
		errorMap.put("message", e.getMessage());
		return ResponseEntity.internalServerError().body(errorMap);
	}
	
	// 抓取因 ObjectMapper 轉換資料型態時所發生的 JsonProcessingException
		@ExceptionHandler({JsonProcessingException.class})
		public ResponseEntity<Map<String, Object>> handleJsonProcessingException(JsonProcessingException e) {
			// 因為在 controller 中的 res 返回的是 code: 數值 和 message: 字串；所以 map 的 value 用 Object 接
			Map<String, Object> errorMap = new HashMap<>();
			// 將轉換資料型態發生的 Exception 代碼歸類為 400
			errorMap.put("code", HttpStatus.BAD_REQUEST.value());
			// 取錯誤訊息
			errorMap.put("message", e.getMessage());
			// 返回的 Http Status Code 為 badRequest(400)
			return ResponseEntity.badRequest().body(errorMap);
		}
		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
		    Map<String, Object> errorMap = new HashMap<>();
		    errorMap.put("code", HttpStatus.NOT_FOUND.value()); // 回傳 404
		    errorMap.put("message", e.getMessage());
		    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
		}
		
		@ExceptionHandler(AccessDeniedException.class)
	    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException e) {
	        Map<String, Object> errorMap = new HashMap<>();
	        errorMap.put("code", HttpStatus.FORBIDDEN.value()); // 403
	        errorMap.put("message", "權限不足，無法執行此操作");
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMap);
	    }
}
