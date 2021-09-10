package com.example.springsecurity2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * システム利用ユーザを表すエンティティクラス。
 */
@Entity
@Data
public class SiteUser {

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ユーザ名 */
    @Size(min = 2, max = 20)
    private String username;

    /** パスワード */
    @Size(min = 4, max = 255)
    private String password;

    /** メールアドレス */
    @NotBlank
    @Email
    private String email;

    /** 性別 */
    private int gender;

    /** 管理者権限 */
    private boolean admin;

    /** ロール */
    private String role;

    /** 有効なユーザかどうか */
    private boolean active = true;

}
