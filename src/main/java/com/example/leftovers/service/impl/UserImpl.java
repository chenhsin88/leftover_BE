package com.example.leftovers.service.impl;

import com.example.leftovers.constants.ResMessage;
import com.example.leftovers.dao.UserDao;
import com.example.leftovers.entity.User;
import com.example.leftovers.exception.AuthenticationFailedException;
import com.example.leftovers.service.ifs.UserService;
import com.example.leftovers.utils.JwtUtil;
import com.example.leftovers.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserImpl(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public BasicRes register(UserRegisterReq req) {
        // 您的註冊邏輯是正確的，我們保留它，只確保使用注入的 passwordEncoder
        if (userDao.existsByEmail(req.getEmail())) {
            return new BasicRes(400, "Email 已被註冊");
        }
        
        User user = new User();
        user.setEmail(req.getEmail());
        
        // 如果是 Google 註冊，密碼可以為 null；否則進行加密
        if (!req.isRegularRegistration()) {
             String encodedPassword = passwordEncoder.encode(req.getPasswordHash());
             user.setPasswordHash(encodedPassword);
        } else {
             user.setPasswordHash(""); // 明確設定 Google 帳號的密碼為 ""
        }

        user.setName(req.getName());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setProfilePictureUrl(req.getProfilePictureUrl());
        user.setActive(true); 
        user.setRole(req.getRole());
        user.setRegularRegistration(req.isRegularRegistration());
        userDao.save(user);

        return new BasicRes(200, "註冊成功");
    }

    /**
     * ★★★ 這就是我們重構後，實現您完整邏輯的 login 方法 ★★★
     */
    @Override
    public LoginResponseVO login(UserLoginReq req) {
        // 1. 根據 email 從資料庫尋找使用者
        Optional<User> optionalUser = userDao.findByEmail(req.getEmail());
        if (optionalUser.isEmpty()) {
            // 為了安全，不提示「帳號不存在」，統一提示錯誤
            throw new AuthenticationFailedException("帳號不存在");
        }
        User user = optionalUser.get();
        
        // ✅ 2. 檢查帳號是否被停權
        if (!user.isActive()) {
            throw new RuntimeException("此帳號已被停權，無法登入，請聯絡管理員");
        }

        // 2. 判斷登入類型，並執行對應的驗證邏輯
        
        // 【情況一：前端發起的是 Google 登入請求】
        // (對應到您前端 googlelogin() 方法，regularRegistration 會是 true)
        if (req.isRegularRegistration()) {
            // 檢查資料庫中的使用者是否也是 Google 帳號
            if (!user.isRegularRegistration()) {
                // 是普通帳號，卻想用 Google 方式登入，拒絕！
            	throw new AuthenticationFailedException("此 Email 已被註冊為一般帳號，請使用密碼登入");
            }
            // 是 Google 帳號，且用 Google 方式登入，直接成功，無需比對密碼。
            return generateTokensAndLoginResponse(user);
        }
        
        // 【情況二：前端發起的是一般帳號密碼登入請求】
        else {
            // 檢查資料庫中的使用者是否是 Google 帳號
            if (user.isRegularRegistration()) {
                // 是 Google 帳號，卻想用密碼登入，拒絕！
                throw new AuthenticationFailedException("此帳號是透過 Google 註冊的，請使用 Google 登入。");
            }

            // 檢查密碼是否為空
            if (req.getPasswordHash() == null || req.getPasswordHash().isBlank()) {
                throw new AuthenticationFailedException("缺少密碼");
            }

            // 比對密碼
            boolean passwordMatches = passwordEncoder.matches(req.getPasswordHash(), user.getPasswordHash());
            if (!passwordMatches) {
                throw new AuthenticationFailedException("帳號或密碼錯誤");
            }
            
            // 密碼比對成功
            return generateTokensAndLoginResponse(user);
        }
    }

    /**
     * 輔助方法：用於生成 Tokens 並打包成 LoginResponseVO
     */
    private LoginResponseVO generateTokensAndLoginResponse(User user) {
        // 將我們自己的 User 實體，轉換成 Spring Security 需要的 UserDetails 物件
        org.springframework.security.core.userdetails.UserDetails userDetails = 
            new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash() != null ? user.getPasswordHash() : "", // 密碼可以是空字串，但不能是 null
                List.of(new SimpleGrantedAuthority(user.getRole())) // 加入角色權限
            );

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new LoginResponseVO(accessToken, refreshToken);
    }

    @Override
    public BasicRes update(UserUpdateReq req) {
        Optional<User> optional = userDao.findById(req.getEmail());
        if (!optional.isPresent()) {
            return new BasicRes(404, "使用者不存在");
        }

        User user = optional.get();

        // 【修改】更新密碼的邏輯
        if (req.getPasswordHash() != null && !req.getPasswordHash().isBlank()) {
            // 1. 檢查是否有提供「目前密碼」
            if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank()) {
                return new BasicRes(400, "若要變更密碼，請提供目前密碼");
            }

            // 2. 驗證「目前密碼」是否正確
            if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPasswordHash())) {
                return new BasicRes(400, "目前密碼不正確");
            }

            // 3. 驗證新密碼格式
            if (!ValidatorUtils.isValidPassword(req.getPasswordHash())) {
                return new BasicRes(400, "密碼格式錯誤：需6-16碼、包含大小寫與數字");
            }
            
            // 4. 所有驗證通過，加密並設定新密碼
            String encodedPassword = passwordEncoder.encode(req.getPasswordHash());
            user.setPasswordHash(encodedPassword);
        }

        if (req.getName() != null && !req.getName().isBlank()) {
            user.setName(req.getName());
        }

        if (req.getPhoneNumber() != null && !req.getPhoneNumber().isBlank()) {
            if (!ValidatorUtils.isValidPhoneNumber(req.getPhoneNumber())) {
                return new BasicRes(400, "手機格式錯誤");
            }
            user.setPhoneNumber(req.getPhoneNumber());
        }

        if (req.getProfilePictureUrl() != null && !req.getProfilePictureUrl().isBlank()) {
            user.setProfilePictureUrl(req.getProfilePictureUrl());
        }

        userDao.save(user);

        return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 驗證更新資料的正規表達
    private static class ValidatorUtils {
        public static boolean isValidPhoneNumber(String phoneNumber) {
            return phoneNumber != null && phoneNumber.matches("^09\\d{8}$");
        }

        public static boolean isValidPassword(String passwordHash) {
            return passwordHash != null && passwordHash.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,16}$");
        }
    }

    @Override
    public UserRoleRes checkEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new UserRoleRes(400, "Email 不可為空");
        }
        String existingEmail = userDao.checkEmail(email);
        if (existingEmail != null && existingEmail.equalsIgnoreCase(email)) {
            // ⚠️ 正確處理 Byte -> Boolean
            Byte regByte = userDao.getRegularRegistration(email);
            boolean isRegular = (regByte != null && regByte == 1);

            String role = userDao.getRole(email);
            return new UserRoleRes(400, "Email 已被註冊", role, isRegular);
        }
        return new UserRoleRes(200, "Email 可使用");
    }
}
