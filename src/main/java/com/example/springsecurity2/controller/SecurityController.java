package com.example.springsecurity2.controller;

import com.example.springsecurity2.model.SiteUser;
import com.example.springsecurity2.repository.SiteUserRepository;
import com.example.springsecurity2.util.Role;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

/**
 * ホームコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class SecurityController {

    /**
     * システム利用ユーザリポジトリ
     * <p>コンストラクタインジェクション
     */
    private final SiteUserRepository siteUserRepository;

    /**
     * パスワード暗号化用エンコーダ
     * <p>コンストラクタインジェクション
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * ログインページを表示。
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * ユーザ情報ページ。ログイン後に遷移してくるパス。
     * 
     * @param loginUser 認証済みのユーザー情報
     */
    @GetMapping("/")
    public String showList(Authentication loginUser, Model model) {
        model.addAttribute("username", loginUser.getName());
        model.addAttribute("role", loginUser.getAuthorities());
        return "user";
    }

    /**
     * ユーザ一覧ページを表示（管理者権限用）。
     */
    @GetMapping("/admin/list")
    public String showAdminList(Model model) {
        model.addAttribute("users", siteUserRepository.findAll());
        return "list";
    }

    /**
     * ユーザ登録ページを表示。
     */
    @GetMapping("/register")
    public String register(@ModelAttribute("user") SiteUser siteUser) {
        return "register";
    }

    /**
     * ユーザ登録処理。
     */
    @PostMapping("/register")
    public String process(@Validated @ModelAttribute("user") SiteUser siteUser, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword()));
        if (siteUser.isAdmin()) {
            siteUser.setRole(Role.ADMIN.name());
        } else {
            siteUser.setRole(Role.USER.name());
        }

        siteUserRepository.save(siteUser);

        return "redirect:/login?register";
    }

}
