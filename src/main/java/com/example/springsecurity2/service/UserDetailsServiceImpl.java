package com.example.springsecurity2.service;

import java.util.HashSet;
import java.util.Set;

import com.example.springsecurity2.model.SiteUser;
import com.example.springsecurity2.repository.SiteUserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security のログイン認証機能で使用するサービスの実装クラス。
 *
 * <p>{@code UserDetailsService#loadUserByUsername()}を実装し、
 * {@code SecurityConfig}の中で Spring Security にこの実装クラスを渡すことで
 * 標準のログイン認証機能が実現される。
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * システム利用ユーザのリポジトリ
     * <p>コンストラクタインジェクション
     */
    private final SiteUserRepository siteUserRepository;

    /**
     * ユーザ名でユーザマスタを検索し、{@code UserDetails} 型の
     * インスタンス（Spring Security標準のユーザー情報）を返却する。
     * 
     * @param username Spring Security がログイン認証で検索するユーザ名
     * @throws UsernameNotFoundException 対象ユーザ名がDBに存在しなかった場合
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ユーザ名でDB検索
        SiteUser siteUser = siteUserRepository.findByUsername(username);

        // 該当ユーザ名がDBに存在しなかった場合
        if (siteUser == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        // Spring Security が使用するユーザ情報（UserDetails）の実装クラスを返す
        return createUserDetails(siteUser);
    }

    /**
     * Spring Security が使用するユーザ情報（UserDetails）の実装クラスを生成する。
     * 
     * @param siteUser ユーザ名でDB検索したSiteUserエンティティ
     * @return Spring Security が使用するユーザ情報（UserDetailsの実装クラス）
     */
    private UserDetails createUserDetails(SiteUser siteUser) {
        // 権限リストの作成
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + siteUser.getRole()));

        // UserDetailsインターフェースの実装クラスUserのインスタンスを返す
        return new User(siteUser.getUsername(), siteUser.getPassword(), grantedAuthorities);
    }

}
