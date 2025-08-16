package com.example.leftovers.vo;



import com.example.leftovers.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FaqCreateReq {
	@NotBlank(message = ConstantsMessage.PARAM_QUESTION_ERROR)
	private String question;
	
	@NotBlank(message = ConstantsMessage.PARAM_ANSWER_ERROR)
	private String answer;

	@NotBlank(message = ConstantsMessage.PARAM_CATEGORY_ERROR)
	private String category;
	
	private String keywords;
	
	@NotNull(message = ConstantsMessage.PARAM_ADMINID_ERROR)
	private int adminId;
	
	 @NotBlank(message = "管理員的 Email 不能為空")
	 private String adminEmail;  // 用於驗證是否為管理員
	
	
//	@NotNull(message = ConstantsMessage.PARAM_CREATE_AT_ERROR)
//	private LocalDateTime createAt;

	

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

//	public LocalDateTime getCreateAt() {
//		return createAt;
//	}
//
//	public void setCreateAt(LocalDateTime createAt) {
//		this.createAt = createAt;
//	}

	
}
