package com.example.leftovers.vo;

import java.util.List;

public class UserRes extends BasicRes{

	private List<UserVo> Vo;
	
	public UserRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public UserRes(int code, String message, List<UserVo> vo) {
		super(code, message);
		Vo = vo;
	}

	public List<UserVo> getVo() {
		return Vo;
	}

	public void setVo(List<UserVo> vo) {
		Vo = vo;
	}




}
