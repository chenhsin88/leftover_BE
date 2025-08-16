
CREATE TABLE IF NOT EXISTS `orders` (
  `user_name` varchar(20) NOT NULL,
  `merchants_id` int NOT NULL,
  `order_id` bigint NOT NULL,
  `total_amount` int NOT NULL,
  `original_total_amount` int DEFAULT NULL,
  `status` enum('pending','picked_up','completed','cancelled_by_user','cancelled_by_merchant') NOT NULL,
  `payment_method_simulated` enum('CREDIT_CARD','CASH') DEFAULT NULL,
  `pickup_code` varchar(45) NOT NULL,
  `notes_to_merchant` varchar(50) DEFAULT NULL,
  `ordered_at` datetime DEFAULT NULL,
  `pickup_confirmed_at` datetime DEFAULT NULL,
  `cancelled_at` datetime DEFAULT NULL,
  `cancellation_reason` text,
  `updated_at` datetime DEFAULT NULL,
  `food_id` int NOT NULL,
  `cancel_status` enum('REJECT','PENDING','ALLOW') DEFAULT NULL,
  `user_email` varchar(45) DEFAULT NULL,
  `reject_reason` text,
  `merchant` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`order_id`,`pickup_code`),
  UNIQUE KEY `order_number_UNIQUE` (`order_id`),
  UNIQUE KEY `pickup_code_UNIQUE` (`pickup_code`)
) ;




CREATE TABLE IF NOT EXISTS `orders_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL,
  `food_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `unit_price` int NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `food_name` varchar(45) NOT NULL,
  `merchant` varchar(45) NOT NULL,
  `merchant_id` int DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  `user_email` varchar(45) DEFAULT NULL,
  `food_price` int DEFAULT NULL,
  `pickup_code` varchar(45) DEFAULT NULL,
  `notes_to_merchant` varchar(100) DEFAULT NULL,
  `cancellation_reason` varchar(45) DEFAULT NULL,
  `reject_reason` varchar(45) DEFAULT NULL,
  `payment_method_simulated` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;




CREATE TABLE IF NOT EXISTS `reviews` (
  `merchant` varchar(50) NOT NULL,
  `rating` int NOT NULL DEFAULT '0',
  `comment` varchar(45) DEFAULT NULL,
  `created_at` varchar(45) DEFAULT NULL,
  `order_id` int NOT NULL DEFAULT '0',
  `user_name` varchar(45) NOT NULL,
  `merchant_id` int NOT NULL,
  `merchant_reply` varchar(500) DEFAULT NULL,
  `merchant_reply_at` datetime DEFAULT NULL,
  PRIMARY KEY (`user_name`,`order_id`,`merchant_id`),
  UNIQUE KEY `order_id_UNIQUE` (`order_id`)
) ;

CREATE TABLE IF NOT EXISTS `order_food_item` (
  `merchants_id` int NOT NULL,
  `order_id` bigint NOT NULL,
  `food_id` int NOT NULL,
  `quantity` int NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `food_name` varchar(45) DEFAULT NULL,
  `food_price` int DEFAULT NULL,
  `merchant` varchar(45) DEFAULT NULL,
  `ordered_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`merchants_id`,`food_id`)
);





CREATE TABLE IF NOT EXISTS `users` (
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `profile_picture_url` varchar(512) DEFAULT NULL,
  `role` enum('customer','merchants','Administrator') NOT NULL,
  `is_active` tinyint NOT NULL,
  `regular_registration` tinyint DEFAULT '0',
  PRIMARY KEY (`email`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`)
);


CREATE TABLE IF NOT EXISTS `merchants` (
  `merchants_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `address_text` varchar(512) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `contact_email` varchar(255) DEFAULT NULL,
  `logo_url` mediumtext,
  `banner_image_url` mediumtext,
  `opening_hours_description` text,
  `map_screenshot_url` mediumtext,
  `map_google_url` mediumtext,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `approved_by_admin_id` int DEFAULT '0',
  `approved_at` datetime DEFAULT NULL,
  `created_by_email` varchar(255) DEFAULT NULL,
  `pickup_instructions` varchar(500) DEFAULT NULL,
  `longitude_and_latitude` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`merchants_id`)
);



CREATE TABLE IF NOT EXISTS `push_notification_tokens` (
  `food_items_id` int NOT NULL DEFAULT '0',
  `merchants_id` int NOT NULL DEFAULT '0',
  `previous_price` int NOT NULL DEFAULT '0',
  `new_price` int DEFAULT NULL,
  `set_time` datetime DEFAULT NULL,
  `push_notifications` tinyint DEFAULT NULL,
  `default_hours` int DEFAULT NULL,
  PRIMARY KEY (`food_items_id`)
);

CREATE TABLE IF NOT EXISTS `food_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchants_id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `image_url` varchar(255) DEFAULT NULL,
  `original_price` int NOT NULL,
  `discounted_price` int NOT NULL,
  `quantity_available` int unsigned NOT NULL DEFAULT '0',
  `pickup_start_time` datetime NOT NULL,
  `pickup_end_time` datetime NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
  );

CREATE TABLE IF NOT EXISTS `faq_items` (
  `question` varchar(500) NOT NULL,
  `answer` varchar(500) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `keywords` text,
  `created_by_admin_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`question`)

) ;

CREATE TABLE IF NOT EXISTS `carts` (
  `id` int NOT NULL,
  `user_email` varchar(100) NOT NULL,
  `merchants_id` int NOT NULL,
  `status` enum('active','converted_to_order','abandoned') DEFAULT 'active',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `food_item_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ;

