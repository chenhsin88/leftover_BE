package com.example.leftovers.controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import com.example.leftovers.vo.LoginResponseVO;
import java.util.Map;
import com.example.leftovers.utils.JwtUtil; // ★ 1. 引入 JwtUtil
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.leftovers.service.ifs.UserService;
import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.UserLoginReq;
import com.example.leftovers.vo.UserRegisterReq;
import com.example.leftovers.vo.UserRoleRes;
import com.example.leftovers.vo.UserUpdateReq;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.Valid;
import com.example.leftovers.dao.UserDao;
import com.example.leftovers.entity.User;
import com.example.leftovers.service.ifs.UserService;
import com.example.leftovers.vo.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/users")
public class UserController {
	 private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserService userService;
	private final UserDao userDao;
	private final JwtUtil jwtUtil;
	@Autowired
    public UserController(UserService userService, UserDao userDao, JwtUtil jwtUtil) { // ★ 3. 修改建構函數
        this.userService = userService;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }
	 @Value("${spring.profiles.active:dev}") // 預設為 'dev' (開發環境)
	    private String activeProfile;
	/**
	 * 註冊<br>
	 * 範例:<br>
	 * { <br>
	 * "email": "teaas4@example.com", <br>
	 * "passwordHash": "12345678Aa",<br>
	 * "name": "測試", <br>
	 * "phoneNumber": "0912344678", <br>
	 * "profilePictureUrl":"https://example.com/avatar.jpg", <br>
	 * "active": true, <br>
	 * "role": "customer",<br>
	 * "regularRegistration": true<br>
	 * }<br>
	 */
	@PostMapping("/register")
	public BasicRes register(@Valid @RequestBody UserRegisterReq req) {
		return userService.register(req);
	}

	/**
	 * regularRegistration:是不是用Email登入，如果是 false 一定要密碼<br>
	 * 登入<br>
	 * req: email、passwordHash<br>
	 * 範例:<br>
	 * { <br>
	 * "email": "teaas4@example.com",<br>
	 * "passwordHash": "12345678Aa"<br>
	 * }<br>
	 * or<br>
	 * { <br>
	 * "email": "teaas4@example.com",<br>
	 * "regularRegistration": true<br>
	 * }<br>
	 */
	 @PostMapping("/login")
		public ResponseEntity<?> login(@Valid @RequestBody UserLoginReq req) {
	        LoginResponseVO loginResponse = userService.login(req);

	        // ★ 2. 判斷是否為生產環境
	        boolean isProduction = "prod".equalsIgnoreCase(activeProfile);

	        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
	                .httpOnly(true)
	                .secure(isProduction) // ★ 3. 動態設定 secure 旗標
	                .path("/")
	                .maxAge(7 * 24 * 60 * 60)
	                .sameSite("Lax")
	                .build();
	        
	        Map<String, String> responseBody = Map.of("accessToken", loginResponse.getAccessToken());

	        return ResponseEntity.ok()
	                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
	                .body(responseBody);
		}
	 
	 
	 @PostMapping("/logout")
	    public ResponseEntity<?> logout() {
	        // 建立一個與登入時同名、同路徑的 Cookie，
	        // 但將它的 maxAge 設為 0，這會命令瀏覽器立即刪除它。
	        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "") // value 可以是空的
	                .httpOnly(true)
	                .secure(false) // 在開發環境中保持 false，與登入時的設定一致
	                .path("/")
	                .maxAge(0) // ★ 關鍵：讓 Cookie 立刻過期
	                .sameSite("Lax") // 與登入時的設定保持一致
	                .build();

	        // 回傳一個成功的 HTTP 回應，並在 Header 中附上這個「刪除 Cookie」的指令
	        return ResponseEntity.ok()
	                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
	                .body(Map.of("message", "已成功登出"));
	    }

	/**
	 * 更新使用者資料（email 為辨識用，不可更改）<br>
	 * 不需要更新的資料可以不用寫入<br>
	 * req: email、passwordHash、name、phoneNumber、profilePictureUrl、active<br>
	 * 範例:<br>
	 * { <br>
	 * "email": "test123@gmail.com", <br>
	 * "passwordHash": "12345678Aa",<br>
	 * "name": "測試", <br>
	 * "phoneNumber": "0997798456", <br>
	 * "profilePictureUrl":"https://example.com/avatar.jpg", <br>
	 * "active": true, <br>
	 * "role": "customer"<br>
	 * }<br>
	 */
	@PostMapping("/update")
	public BasicRes updateUser(@Valid @RequestBody UserUpdateReq req) {
		return userService.update(req);
	}

	/**
	 * 確認有沒有信箱<br>
	 * http://localhost:8080/users/checkEmailExists/{輸入信箱}<br>
	 * 範例:<br>
	 * http://localhost:8080/users/checkEmailExists/teaas@example.com<br>
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping("/checkEmailExists/{email}")
	public UserRoleRes checkEmail(@PathVariable("email") String email) {
		return userService.checkEmail(email);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
	    // 1. 從請求的 Cookies 中尋找 "refreshToken"
	    if (request.getCookies() == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "請求中未包含任何 Cookie"));
	    }

	    // 【修正點】將變數改名為 incomingRefreshTokenCookie
	    Cookie incomingRefreshTokenCookie = Arrays.stream(request.getCookies())
	            .filter(cookie -> "refreshToken".equals(cookie.getName()))
	            .findFirst()
	            .orElse(null);

	    // 使用改名後的新變數
	    if (incomingRefreshTokenCookie == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "請求中未包含 Refresh Token"));
	    }

	    // 使用改名後的新變數
	    String refreshToken = incomingRefreshTokenCookie.getValue();
	    try {
	        String userEmail = jwtUtil.extractUsername(refreshToken);

	        User user = userDao.findByEmail(userEmail)
	                .orElseThrow(() -> new RuntimeException("Refresh token 中的使用者不存在"));
	        
	        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
	                user.getEmail(),
	                user.getPasswordHash() != null ? user.getPasswordHash() : "",
	                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole()))
	        );

	        if (jwtUtil.isTokenValid(refreshToken, userDetails)) {
	            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
	            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

	            // 這裡的變數名稱維持不變，因為它在不同的範圍內
	            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
	                    .httpOnly(true)
	                    .secure("prod".equalsIgnoreCase(activeProfile))
	                    .path("/")
	                    .maxAge(7 * 24 * 60 * 60)
	                    .sameSite("Lax")
	                    .build();

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
	                    .body(Map.of("accessToken", newAccessToken));
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh Token 無效或已過期"));
	        }
	    } catch (Exception e) {
	        logger.error("Refresh Token 處理失敗！Token: [{}], 錯誤類型: {}, 錯誤訊息: {}", 
	                 refreshToken, e.getClass().getName(), e.getMessage());
	             
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("error", "Refresh Token 處理失敗: " + e.getMessage()));
	    }
	}
	
	@GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未授權"));
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();
        
        Optional<User> userOptional = userDao.findByEmail(userEmail);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "找不到使用者資料"));
        }
        
        User userEntity = userOptional.get();
        
        //若使用者未啟用（isActive = false 或 0），直接回 403
        if (!userEntity.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "帳號已被停權，請聯繫管理員"));
        }
        
        UserVo userVo = new UserVo();
        // ... 將 userEntity 的資料設定到 userVo ...
        userVo.setEmail(userEntity.getEmail());
        userVo.setName(userEntity.getName());
        userVo.setPhoneNumber(userEntity.getPhoneNumber());
        userVo.setProfilePictureUrl(userEntity.getProfilePictureUrl());
        userVo.setRole(userEntity.getRole());
        userVo.setRegularRegistration(userEntity.isRegularRegistration());
        userVo.setRating(userEntity.getRating());
        
        

        // ★ 5. 生成一個新的 Access Token
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);

        // ★ 6. 將 UserVo 和新的 accessToken 一起回傳
        return ResponseEntity.ok(Map.of(
            "user", userVo,
            "accessToken", newAccessToken
        ));
    }
}
