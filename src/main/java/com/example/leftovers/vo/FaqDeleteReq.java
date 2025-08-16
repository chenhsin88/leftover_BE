package com.example.leftovers.vo;

import jakarta.validation.constraints.NotBlank;

public class FaqDeleteReq {

	@NotBlank(message = "問題(question)不能為空")
    private String question;
	
	 @NotBlank(message = "管理員的 Email 不能為空")
	 private String adminEmail;  // 用於驗證是否為管理員

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	
	
}
