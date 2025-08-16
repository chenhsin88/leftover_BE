package com.example.leftovers.service.ifs;

import com.example.leftovers.vo.BasicRes;
import com.example.leftovers.vo.MerchantsAndFoodItemsRes;
import com.example.leftovers.vo.MerchantsDistanceReq;
import com.example.leftovers.vo.MerchantsDistanceRes;
import com.example.leftovers.vo.MerchantsReq;
import com.example.leftovers.vo.MerchantsRes;
import com.example.leftovers.vo.MerchantsUpdateReq;
import com.example.leftovers.vo.MerchantsWithinRangeReq;
import com.example.leftovers.vo.MerchantsDetailRes;
public interface MerchantsService {

	public BasicRes register(MerchantsReq req); //註冊
	
	public BasicRes updateMerchants(MerchantsUpdateReq req);//更新資料
	
	public MerchantsRes getAllMerchantsByEmail(MerchantsUpdateReq req); //用管理員信箱取得所有資料
	
	public MerchantsRes getAllMerchantsByMerchantId(int MerchantsId); //用店家ID取得所有資料
	
	public MerchantsRes getAllMerchants();//取得所有商家資訊	
	
	public MerchantsRes getMerchantsWithinRange(MerchantsWithinRangeReq req);//取得範圍內的所有商家資訊

	public MerchantsDistanceRes getSstoreDistance(MerchantsDistanceReq req);//店家的距離
	
	public MerchantsAndFoodItemsRes getMerchantsAndFoodWithinRange(MerchantsWithinRangeReq req);//取得所有商家資訊與食物
	
	public MerchantsDetailRes getMerchantDetailsWithReviews(int merchantId);//取得商家詳細資訊與評論列表
	
	/**
     * 【AI專用】查詢該範圍內所有店家與商品的資訊，但【不包含】商品圖片 URL。
     * 執行與 getMerchantsAndFoodWithinRange 相同的資料庫查詢，
     * 但在回傳前，會將所有商品的圖片資料清除(設為null)。
     * * @param req 包含使用者座標和查詢範圍距離的請求物件
     * @return 包含商家和已清除圖片URL的商品列表的回應物件
     */
    public MerchantsAndFoodItemsRes getLightweightMerchantsWithinRange(MerchantsWithinRangeReq req);
	
}
