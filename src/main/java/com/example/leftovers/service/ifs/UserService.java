package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.LoginResponseVO;
import com.example.leftovers.vo.UserLoginReq;
import com.example.leftovers.vo.UserRegisterReq;
import com.example.leftovers.vo.UserRes;
import com.example.leftovers.vo.UserRoleRes;
import com.example.leftovers.vo.UserUpdateReq;


public interface UserService {

	public BasicRes register(UserRegisterReq req); //註冊
	
//	public UserRes login(UserLoginReq req); //登入
	public LoginResponseVO login(UserLoginReq req);
	
	public BasicRes update(UserUpdateReq req);//編輯
	
	public UserRoleRes checkEmail(String email);//確認是否已有信箱

}
