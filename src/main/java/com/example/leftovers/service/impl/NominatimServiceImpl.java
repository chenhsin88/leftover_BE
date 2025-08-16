package com.example.leftovers.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.leftovers.service.ifs.NominatimService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NominatimServiceImpl implements NominatimService {

    @Override
    public double[] getCoordinatesFromAddress(String address) throws Exception {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("地址不能為空");
        }

//        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);//轉成安全格式，但用了會有問題
        String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&limit=1";

        System.out.println("請求 URL: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "MySpringApp/1.0 (your_email@example.com)"); // 替換成你自己的 email
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            url, HttpMethod.GET, requestEntity, String.class
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

        if (lat == 0.0 || lon == 0.0) {
            throw new Exception("經緯度為0");
        }

        return new double[]{lon, lat};
    }
    

    public class AddressUtils {
        public static String normalize(String input) {
            if (input == null || input.trim().isEmpty()) {
                return "";
            }

            String city = "", district = "", road = "", number = "";

            int cityIdx = input.indexOf("市");
            if (cityIdx == -1) cityIdx = input.indexOf("縣");
            if (cityIdx != -1) {
                city = input.substring(0, cityIdx + 1);
                input = input.substring(cityIdx + 1);
            }

            int districtIdx = input.indexOf("區");
            if (districtIdx != -1) {
                district = input.substring(0, districtIdx + 1);
                input = input.substring(districtIdx + 1);
            }

            int numberIdx = input.indexOf("號");
            if (numberIdx != -1) {
                String numberPart = input.substring(0, numberIdx);
                number = numberPart.replaceAll(".*?(\\d+-?\\d*)$", "$1") + "號";
                input = input.substring(0, numberIdx);
            }

            road = input.replaceAll("\\d+-?\\d*$", "").trim();

            return String.format("%s %s, %s, %s, Taiwan", road, number, district, city);
        }
    }

}
