package com.example.springsecurity2.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import com.example.springsecurity2.model.SiteUser;
import com.example.springsecurity2.util.Role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ホームコントローラのテストクラス。
 * 
 * <p>MockMvcを使用してテストする。
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional // テスト終了後自動的にロールバックする
class SecurityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("ログインページが表示されること")
    void whenAccessTheLoginPage_expectToSeeLoginPage() throws Exception {
        // Act（実行）& Assert（検証）
        mockMvc
                // リクエスト送信
                .perform(
                        // リクエスト
                        get("/login"))
                // 検証
                // HTTPステータスが OK（200）であること
                .andExpect(status().isOk())
                // HTMLの内容に指定文字列を含んでいること
                .andExpect(content().string(containsString("ユーザ管理システム")))
                // 指定したViewが表示されていること
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("ログイン後にユーザ情報ページが表示されること")
    @WithMockUser // モックユーザでログイン
    void whenLogedIn_expectToSeeUserInfoPage() throws Exception {
        // Act（実行）& Assert（検証）
        mockMvc
                // リクエスト送信
                .perform(
                        // リクエスト
                        get("/"))
                // 検証
                // HTTPステータスが OK（200）であること
                .andExpect(status().isOk())
                // HTMLの内容に指定文字列を含んでいること
                .andExpect(content().string(containsString("ログイン情報")))
                // 指定したViewが表示されていること
                .andExpect(view().name("user"));
    }

    @Test
    @DisplayName("ユーザ登録ページで項目未入力の場合、バリデーションエラーが発生すること")
    void whenThereIsRegistrationError_expectToSeeErrors() throws Exception {
        // Act（実行）& Assert（検証）
        mockMvc
                // リクエスト送信
                .perform(
                        // リクエスト
                        post("/register")
                                // リクエストボディ（Model属性）の設定（項目未入力を再現）
                                .flashAttr("user", new SiteUser())
                                // CSRFトークンを自動挿入
                                .with(csrf()))
                // 検証
                // バリデーションエラーが発生すること
                .andExpect(model().hasErrors())
                // 指定したViewが表示されていること
                .andExpect(view().name("register"));
    }

    @Test
    @DisplayName("管理者ユーザが登録できること")
    void whenRegisteringAsAdminUser_expectToSucceed() throws Exception {
        // Arrange（準備）
        SiteUser adminUser = new SiteUser();
        adminUser.setUsername("AdminUser01");
        adminUser.setPassword("pass");
        adminUser.setEmail("adminuser01@example.com");
        adminUser.setGender(1);
        adminUser.setRole(Role.ADMIN.name());

        // Act（実行）& Assert（検証）
        mockMvc
                // リクエスト送信
                .perform(
                        // リクエスト
                        post("/register")
                                // リクエストボディ（ModelAttribute）設定
                                .flashAttr("user", adminUser)
                                // CSRFトークン自動挿入
                                .with(csrf()))
                // 検証
                // バリデーションエラーが発生しないこと
                .andExpect(model().hasNoErrors())
                // 指定URLにリダイレクトすること
                .andExpect(redirectedUrl("/login?register"))
                // HTTPステータスが Found（302（リダイレクト））であること
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("管理者ユーザでログイン時、ユーザ一覧ページが表示できること")
    @WithMockUser(username = "adminuser", roles = "ADMIN") // モックユーザでログイン。実在しないユーザでもOK
    void whenLogedInAsAdminUser_expectToSeeListOfUsers() throws Exception {
        // Act（実行）& Assert（検証）
        mockMvc
                // リクエスト送信
                .perform(
                        // リクエスト
                        get("/admin/list"))
                // 検証
                // HTTPステータスが OK（200）であること
                .andExpect(status().isOk())
                // HTMLの内容に指定文字列を含んでいること
                .andExpect(content().string(containsString("ユーザ一覧")))
                // 指定したViewが表示されていること
                .andExpect(view().name("list"));
    }

}
