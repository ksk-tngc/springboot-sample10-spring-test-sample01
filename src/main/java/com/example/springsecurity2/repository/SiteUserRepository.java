package com.example.springsecurity2.repository;

import com.example.springsecurity2.model.SiteUser;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * システム利用ユーザのリポジトリインターフェース。
 */
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {

    /**
     * ユーザ名で検索するクエリメソッド。
     * 
     * @param username 検索対象のユーザ名
     * @return SiteUserエンティティ
     */
    SiteUser findByUsername(String username);

    /**
     * ユーザ名で存在チェックするクエリメソッド。
     * 
     * @param username 検索対象ユーザ名
     * @return 存在する場合true
     */
    boolean existsByUsername(String username);

}
