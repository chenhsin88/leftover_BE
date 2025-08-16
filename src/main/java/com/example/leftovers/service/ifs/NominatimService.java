package com.example.leftovers.service.ifs;

public interface NominatimService {

	// 用地址取得座標的方法
	public double[] getCoordinatesFromAddress(String address) throws Exception;
	
}
