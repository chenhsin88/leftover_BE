
INSERT INTO orders_history (
    order_id, food_id, quantity, unit_price, food_price, created_at,
    food_name, merchant, merchant_id, user_email, user_name, pickup_code, notes_to_merchant
) VALUES
(2001, 101, 2, 100, 120, '2023-01-15 10:00:00', '雞排', '小吃部', 6, 'test1@mail.com', '阿明', NULL, NULL),
(2002, 102, 1, 80, 90, '2023-02-20 12:30:00', '魯肉飯', '小吃部', 6, 'test2@mail.com', '小華', NULL, NULL),
(2003, 103, 3, 50, 60, '2023-03-10 15:00:00', '滷味', '小吃部', 6, 'test3@mail.com', '阿偉', NULL, NULL),
(2004, 104, 1, 100, 110, '2024-01-05 11:00:00', '豬排飯', '小吃部', 6, 'test4@mail.com', '小美', NULL, NULL),
(2005, 105, 2, 70, 80, '2024-02-14 18:00:00', '排骨飯', '小吃部', 6, 'test5@mail.com', '小傑', NULL, NULL),
(2006, 106, 1, 60, 65, '2024-04-22 13:00:00', '便當', '小吃部', 6, 'test6@mail.com', '小綠', NULL, NULL),
(2007, 107, 2, 90, 100, '2025-06-01 09:00:00', '炒飯', '小吃部', 6, 'test7@mail.com', '阿忠', NULL, NULL),
(2008, 108, 3, 55, 70, '2025-06-17 14:30:00', '炒麵', '小吃部', 6, 'test8@mail.com', '阿順', NULL, NULL),
(2009, 109, 1, 120, 130, '2025-07-10 12:00:00', '燒肉飯', '小吃部', 6, 'test9@mail.com', '小珍', NULL, NULL),
(2010, 110, 2, 100, 110, '2025-07-21 17:30:00', '雞腿飯', '小吃部', 6, 'test10@mail.com', '小林', NULL, NULL),
(2011, 111, 1, 85, 95, '2026-01-10 11:45:00', '蛋包飯', '小吃部', 6, 'test11@mail.com', '小涵', NULL, NULL),
(2012, 112, 2, 65, 70, '2026-03-05 08:30:00', '鍋燒意麵', '小吃部', 6, 'test12@mail.com', '小莉', NULL, NULL),
(2013, 113, 3, 75, 85, '2026-06-18 19:15:00', '義大利麵', '小吃部', 6, 'test13@mail.com', '小李', NULL, NULL),
(2014, 114, 1, 90, 95, '2027-02-08 10:20:00', '牛肉麵', '小吃部', 6, 'test14@mail.com', '阿強', NULL, NULL),
(2015, 115, 2, 60, 65, '2027-05-22 13:40:00', '米粉湯', '小吃部', 6, 'test15@mail.com', '小芳', NULL, NULL),
(2016, 116, 1, 70, 80, '2027-08-01 16:00:00', '肉燥飯', '小吃部', 6, 'test16@mail.com', '阿傑', NULL, NULL),
(2017, 117, 2, 55, 60, '2028-03-12 11:10:00', '燴飯', '小吃部', 6, 'test17@mail.com', '小敏', NULL, NULL),
(2018, 118, 3, 100, 110, '2028-09-30 18:25:00', '咖哩飯', '小吃部', 6, 'test18@mail.com', '小婷', NULL, NULL),
(2019, 119, 1, 95, 100, '2029-01-06 09:55:00', '牛丼', '小吃部', 6, 'test19@mail.com', '阿宏', NULL, NULL),
(2020, 120, 2, 105, 115, '2029-07-17 13:41:53', '吃很飽', '小吃部', 6, 'test20@mail.com', '測試使用者', NULL, NULL);

DELETE FROM merchants WHERE merchants_id IN (10, 20);
INSERT INTO merchants (
    merchants_id, name, description, address_text, phone_number, contact_email,
    logo_url, banner_image_url, opening_hours_description, map_screenshot_url, map_google_url, 
    is_active, approved_by_admin_id, approved_at, merchantscol, 
    created_by_email, pickup_instructions, longitude_and_latitude
) VALUES
(10, '美味餐廳', '主打炸雞的餐廳', '台北市信義區', '0912345678', 'meishi@mail.com',
 NULL, NULL, NULL, NULL, NULL,
 true, 1, NULL, NULL,
 'owner1@mail.com', NULL, NULL),
(20, '麵館小吃', '道地牛肉麵', '台北市大安區', '0987654321', 'mian@mail.com',
 NULL, NULL, NULL, NULL, NULL,
 true, 1, NULL, NULL,
 'owner2@mail.com', NULL, NULL);

INSERT INTO food_items (
  id, merchants_id, name, description, image_url,
  original_price, discounted_price, quantity_available,
  pickup_start_time, pickup_end_time, category,
  is_active, created_at, updated_at
) VALUES
(102, 10, '香酥雞排', '外酥內嫩的炸雞排', 'http://example.com/chicken_steak.jpg',
 150, 120, 30,
 NOW(), DATE_ADD(NOW(), INTERVAL 8 HOUR), '炸物',
 1, NOW(), NOW()),
(103, 10, '涼拌黃瓜', '清爽開胃小菜', 'http://example.com/cucumber.jpg',
 80, 70, 50,
 NOW(), DATE_ADD(NOW(), INTERVAL 8 HOUR), '小菜',
 1, NOW(), NOW()),
(201, 20, '牛肉麵', '經典紅燒牛肉麵', 'http://example.com/beef_noodle.jpg',
 200, 180, 20,
 NOW(), DATE_ADD(NOW(), INTERVAL 8 HOUR), '麵食',
 1, NOW(), NOW()),
(202, 20, '炸醬麵', '濃郁炸醬麵', 'http://example.com/zhajiangmian.jpg',
 120, 100, 40,
 NOW(), DATE_ADD(NOW(), INTERVAL 8 HOUR), '麵食',
 1, NOW(), NOW());

ON DUPLICATE KEY UPDATE 
  name = VALUES(name),
  description = VALUES(description),
  image_url = VALUES(image_url),
  original_price = VALUES(original_price),
  discounted_price = VALUES(discounted_price),
  quantity_available = VALUES(quantity_available),
  pickup_start_time = VALUES(pickup_start_time),
  pickup_end_time = VALUES(pickup_end_time),
  category = VALUES(category),
  is_active = VALUES(is_active),
  updated_at = NOW();

DELETE FROM carts WHERE id IN (1, 2, 3);
INSERT INTO carts (id, user_email, merchants_id, status, created_at, updated_at, food_item_id, quantity)
VALUES
(1, 'user1@mail.com', 10, 'active', NOW(), NOW(), 101, 2),  
(2, 'user1@mail.com', 10, 'active', NOW(), NOW(), 102, 1),  
(3, 'user1@mail.com', 20, 'active', NOW(), NOW(), 201, 1);
