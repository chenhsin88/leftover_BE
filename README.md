# Leftovers  剩食平台 X 食在不浪費

「Leftovers」是一個向國外Too good To Go 致敬的剩食平台，旨在減少食物浪費，連結消費者、商家，讓剩餘餐食能夠更有效率地被利用。  
本專案採用 **Angular 17 前端** 與 **Spring Boot 後端（MySQL）**，並整合 **地圖服務 / 視覺動畫 / 自動化工作流 / 支付系統**。

---

## 專案功能

### ◆ 前台使用者(消者者)

- 簡易會員註冊流程，包含google登入
- 登入後即可定位，並根據定位推播附近商店、商品
- 快速搜尋分類美食
- 透過條件篩選或自訂公里數，以列表模式或地圖模式，查看商店、商品分佈地點
- 加入購物車內，下單支援現金現場結帳及線上信用卡支付
- 個人即時、歷史訂單管理和查看
- 針對已完成訂單給店家評論
- AI助理，協助搜尋及下單
- 明暗模式
- 個人資料修改
- 小鈴鐺即時通知推播商品

### ◆ 後台使用者(商家)

- 簡易會員註冊流程
- 建立商家
- 商店資訊管理
- 商品上下架、編輯、刪除
- 推播商品設定
- 訂單管理
- 歷史訂單查看
- 營收查看
- 顧客評論管理內，可新增、編輯、刪除店家留言
- 明暗模式
- 個人資料修改
- 小鈴鐺即時通知新進訂單

---

## 使用技術

### 前端 Frontend
- Angular 17
- Angular Material
- Chart.js
- daisyUI
- TailwindCSS
- Carto
- GSAP
- RxJS
- TypeScript / SCSS / HTML


### 後端 Backend
- Spring Boot (Java)
- MySQL Database
- JPA / Hibernate
- RESTful API
- JWT
- n8n
- NewebPay

---

## 專案結構

### 前端 Frontend（[Repo](https://github.com/chenhsin88/Leftovers.git)）

### 後端 Backend（[Repo](https://github.com/chenhsin88/leftover_BE.git)）

---

## 畫面示意

### ◆ 平台首頁
<img width="300" height="200" alt="image" src="https://github.com/user-attachments/assets/81e0deb3-a487-4786-b1a6-5caca8bb3b05" />

### ◆ 定位功能
<img width="400" height="300" alt="image" src="https://github.com/user-attachments/assets/0d1e0a21-04b1-4d1a-b07c-85152f69f0a2" />

### ◆ 管理者-新增、編輯題目

<img width="400" height="300" alt="image" src="https://github.com/user-attachments/assets/ccb7a90c-6a47-49d9-a478-4653f33bbfa6" />

### ◆ 管理者-問卷填寫記錄列表
<img width="360" height="300" alt="localhost_4200_result_feedbackList_id=1" src="https://github.com/user-attachments/assets/9003810a-8cfb-49f1-8158-717dc9147e9e" />

### ◆ 管理者-查看使用者填答紀錄
<img width="300" height="450" alt="1" src="https://github.com/user-attachments/assets/caccddd5-1c97-4388-838b-177bcc47c041" />

### ◆ 用戶/管理者-統計圖表頁面
<img width="300" height="450" alt="localhost_4200_result_count_id=1" src="https://github.com/user-attachments/assets/62c0d118-fa7a-429c-9e49-7b0978526fb6" />

### ◆ 用戶-作答頁面
<img width="300" height="450" alt="localhost_4200_answer_id=2" src="https://github.com/user-attachments/assets/45bdaa8d-e343-40b1-9b49-18c95e777d2d" />


