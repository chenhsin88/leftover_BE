package com.example.leftovers.vo;


import jakarta.validation.constraints.NotBlank;

public class FaqUpdateReq {

	@NotBlank(message = "問題不能為空")
	private String question; // 只是用來找資料，不會修改
	
	private String answer;
	
	private String category;
	
	private String keywords;
	
	private int adminId;
	
	 @NotBlank(message = "管理員的 Email 不能為空")
	 private String adminEmail;  // 用於驗證是否為管理員
	
	
//	@NotBlank(message = ConstantsMessage.PARAM_UPDATE_AT_ERROR)
//	private LocalDateTime updateAt;


	

	public String getQuestion() {
		return question;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

//	public LocalDateTime getUpdateAt() {
//		return updateAt;
//	}
//
//	public void setUpdateAt(LocalDateTime updateAt) {
//		this.updateAt = updateAt;
//	}

}
