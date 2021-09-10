package com.example.springsecurity2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import com.example.springsecurity2.model.SiteUser;
import com.example.springsecurity2.repository.SiteUserRepository;
import com.example.springsecurity2.util.Role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * ユーザ詳細サービスのテストクラス。
 */
@SpringBootTest
@Transactional // テスト終了後自動的にロールバックする
class UserDetailsServiceImplTest {

    @Autowired
    SiteUserRepository siteUserRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    @DisplayName("ユーザ名が存在する場合、ユーザ詳細を取得できること")
    void whenUsernameExists_expectToGetUserDetails() {
        // Arrange（準備）
        SiteUser user = new SiteUser();
        user.setUsername("TestUser");
        user.setPassword("pass");
        user.setEmail("testuser@example.com");
        user.setRole(Role.ADMIN.name());
        siteUserRepository.save(user);

        // Act（実行）
        UserDetails actual = userDetailsServiceImpl.loadUserByUsername("TestUser");

        // Assert（検証）
        assertEquals(user.getUsername(), actual.getUsername()); // assertEquals()の書き方
        assertThat(actual.getUsername()).isEqualTo(user.getUsername()); // assertThat()の書き方
    }

    @Test
    @DisplayName("ユーザ名が存在しない場合、例外がスローされること")
    void whenUsernameDoesNotExist_throwException() {
        // Act（実行）& Assert（検証）
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("no_such_user");
        });
    }

}
