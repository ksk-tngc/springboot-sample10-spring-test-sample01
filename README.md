## 概要

Spring Test のサンプルプロジェクトです。  

JUnit 5 と MockMvc で Spring Boot アプリケーショ（Spring MVC や Spring Security）をテストします。

※ [Spring Security のサンプルアプリ](https://github.com/ksk-tngc/springboot-sample09-spring-security-sample02)をテストします。

## JUnit テスト結果（VSCode）
<img width="1000" src="https://user-images.githubusercontent.com/59589496/132934065-c3472dbf-681a-4edf-8cc9-f706e6fedf97.png">  

## テスト対象画面

### ログインページ
<img width="600" src="https://user-images.githubusercontent.com/59589496/132644649-951e8995-3f77-4e3b-8916-e533e6a47ed3.png">  

### ユーザ登録ページ
<img width="600" src="https://user-images.githubusercontent.com/59589496/132644778-7a148aae-02b9-484b-98a3-a20271d1674d.png">  

### ログイン情報ページ
ログインユーザを表示するページ  
<img width="1000" src="https://user-images.githubusercontent.com/59589496/132645013-de3b6b4c-625c-40bf-8622-4e2ba2a2dd0c.png">  

### ユーザ一覧ページ
管理者（ADMIN）権限のみ表示可（認可制御）  
<img width="1000" src="https://user-images.githubusercontent.com/59589496/132645340-deca83da-147c-4ff4-b8b7-9ad4bf25b523.png">  

## フォルダ構成

<img width="320" src="https://user-images.githubusercontent.com/59589496/132878205-090fdf8a-e185-4373-89c3-29856556f9e7.png">  

## 依存関係

* Spring Boot DevTools
* Thymeleaf
* Thymeleaf Layout Dialect
* Spring Web
* Validation
* Spring Data JPA
* Spring Security
* Spring Test
* H2 Database
* Lombok
* WebJars
  - Bootstrap
  - Bootstrap Icons
  - DataTables
  - DataTables Plugins
  - DataTables Buttons
