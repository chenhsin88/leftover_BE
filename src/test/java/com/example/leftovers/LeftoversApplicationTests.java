	package com.example.leftovers;
	
	import static org.junit.jupiter.api.Assertions.assertNotEquals;
	import static org.junit.jupiter.api.Assertions.fail;
	
	import org.junit.jupiter.api.Test;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.http.HttpEntity;
	import org.springframework.http.HttpHeaders;
	import org.springframework.http.HttpMethod;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.client.RestTemplate;
	
	import com.fasterxml.jackson.databind.JsonNode;
	import com.fasterxml.jackson.databind.ObjectMapper;
	
	@SpringBootTest
	public class LeftoversApplicationTests {
	
	    @Test
	    public void testGetCoordinatesFromAddress() throws Exception {
	        System.out.println("開始測試 getCoordinatesFromAddress");
	
	        String address = normalizeAddress("台北市中正區信義路一段100號");
	        try {
	            double[] coords = getCoordinatesFromNominatim(address);
	            System.out.println("經度: " + coords[0] + ", 緯度: " + coords[1]);
	
	            assertNotEquals(0, coords[0], "經度不應為0");
	            assertNotEquals(0, coords[1], "緯度不應為0");
	
	            System.out.println("測試完成");
	
	        } catch (Exception e) {
	            e.printStackTrace();
	            fail("測試失敗，拋出例外: " + e.getMessage());
	        }
	    }
	
	    
	    public double[] getCoordinatesFromNominatim(String address) throws Exception {
	        RestTemplate restTemplate = new RestTemplate();
	
//	        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
	        String url = "https://nominatim.openstreetmap.org/search?q=" +address+ "&format=json&limit=1";
	
//	        System.out.println("請求 URL: " + url);
	
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("User-Agent", "MySpringApp/1.0 (your_email@example.com)");
	        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
	
	        ResponseEntity<String> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                requestEntity,
	                String.class
	        );
	
	        String json = response.getBody();
	        System.out.println("API 回傳資料: " + json);
	
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(json);
	
	        if (!root.isArray() || root.size() == 0) {
	            throw new Exception("查無地址對應的座標");
	        }
	
	        JsonNode location = root.get(0);
	        double lat = location.path("lat").asDouble();
	        double lon = location.path("lon").asDouble();
	
//	        System.out.println("經度：" + lon + "，緯度：" + lat);
	
	        if (lat == 0.0 || lon == 0.0) {
	            throw new Exception("經緯度為0");
	        }
	
	        return new double[]{lon, lat};
	    }
	
	    public static String normalizeAddress(String input) {
	        if (input == null || input.trim().isEmpty()) {
	            return "";
	        }
	
	        String city = "";
	        String district = "";
	        String road = "";
	        String number = "";
	
	        // 1. 擷取 city（市/縣）
	        int cityIdx = input.indexOf("市");
	        if (cityIdx == -1) {
	            cityIdx = input.indexOf("縣");
	        }
	        if (cityIdx != -1) {
	            city = input.substring(0, cityIdx + 1);
	            input = input.substring(cityIdx + 1); // 移除已處理部分
	        }
	
	        // 2. 擷取 district（區）
	        int districtIdx = input.indexOf("區");
	        if (districtIdx != -1) {
	            district = input.substring(0, districtIdx + 1);
	            input = input.substring(districtIdx + 1);
	        }
	
	        // 3. 擷取 number（號）
	        int numberIdx = input.indexOf("號");
	        if (numberIdx != -1) {
	            // 號碼前可能是巷弄/樓層等，這裡簡單取號前數字
	            String numberPart = input.substring(0, numberIdx);
	            number = numberPart.replaceAll(".*?(\\d+-?\\d*)$", "$1") + "號";
	            input = input.substring(0, numberIdx); // 擷取完後移除
	        }
	
	        // 4. 剩下就是 road（路名）
	        road = input.replaceAll("\\d+-?\\d*$", "").trim(); // 去掉尾端號碼
	
	        return String.format("%s %s, %s, %s, Taiwan", road, number, district, city);
	    }
	    
	}
