package com.example.leftovers.vo;

import java.util.List;

public class ReviewsGetByMerchantsRes extends BasicRes {

	private List<ReviewsVo> reviewsVo;

	public ReviewsGetByMerchantsRes() {
		super();
	}

	public ReviewsGetByMerchantsRes(int code, String message) {
		super(code, message);

	}

	public ReviewsGetByMerchantsRes(int code, String message, List<ReviewsVo> reviewsVo) {
		super(code, message);
		this.reviewsVo = reviewsVo;
	}

	public List<ReviewsVo> getReviewsVo() {
		return reviewsVo;
	}

	public void setReviewsVo(List<ReviewsVo> reviewsVo) {
		this.reviewsVo = reviewsVo;
	}

}
