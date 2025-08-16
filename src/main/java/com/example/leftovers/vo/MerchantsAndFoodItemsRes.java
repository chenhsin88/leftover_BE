package com.example.leftovers.vo;

import java.util.List;

public class MerchantsAndFoodItemsRes extends BasicRes{
	
	private List<MerchantWithFoodsVo> vo;

	public MerchantsAndFoodItemsRes() {
		super();
	}

	public MerchantsAndFoodItemsRes(int code, String message) {
		super(code, message);
	}

	public MerchantsAndFoodItemsRes(int code, String message, List<MerchantWithFoodsVo> vo) {
		super(code, message);
		this.vo = vo;
	}

	public List<MerchantWithFoodsVo> getVo() {
		return vo;
	}

	public void setVo(List<MerchantWithFoodsVo> vo) {
		this.vo = vo;
	}
	
	// This list holds the merchants and their associated food items.
    private List<MerchantWithFoodsVo> merchantsWithFoods;

	  /**
     * This is the getter method that was missing.
     * Once this class is created, your `MerchantsServiceImpl` will be able to find and call this method.
     * @return A list of merchants, each containing their own list of food items.
     */
    public List<MerchantWithFoodsVo> getMerchantsWithFoods() {
        return merchantsWithFoods;
    }

    public void setMerchantsWithFoods(List<MerchantWithFoodsVo> merchantsWithFoods) {
        this.merchantsWithFoods = merchantsWithFoods;
    }
	
}
