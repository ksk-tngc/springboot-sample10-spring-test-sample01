package com.example.springsecurity2.config;

import com.example.springsecurity2.util.Role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

/**
 * セキュリティ設定クラス。
 * 
 * <p>Spring Securityの設定を行う。
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Spring Security 標準のユーザ情報検索用サービス
     * <p>コンストラクタインジェクション
     */
    private final UserDetailsService userDetailsService;

    /**
     * パスワードの暗号化用に、BCrypt（ビー・クリプト）エンコーダを取得。
     * <p>Spring Securityのログイン認証はパスワードの暗号化必須。
     *
     * @return BCryptエンコーダ
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * セキュリティ対象外の設定
     */
    @Override
    public void configure(WebSecurity web) throws Exception {

        // セキュリティ設定を無視するパスを指定
        // 通常、cssやjs、imgなどの静的リソースを指定する （ログイン認証せずにアクセス可能とする）
        web.ignoring() // セキュリティを無視
                .antMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/h2-console/**");

        // 以下のように分けて記述してもOK
        // web.ignoring() // セキュリティ対象外設定
        //         .antMatchers("/css/**") // css
        //         .antMatchers("/js/**") // js
        //         .antMatchers("/img/**") // img
        //         .antMatchers("/webjars/**") // WebJars
        //         .antMatchers("/h2-console/**"); // H2コンソール
    }

    /**
     * セキュリティの各種設定
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // ログイン認証不要なページの設定
        http.authorizeRequests() // 
                .antMatchers("/login", "/register").permitAll() // 直リンクOK
                // 分けて書いてもOK
                // .antMatchers("/login").permitAll() // 直リンクOK
                // .antMatchers("/register").permitAll() // 直リンクOK
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name()) // "/admin/〜"は ADMIN ユーザのみアクセス可（認可設定）
                // .antMatchers("/admin/**").hasAuthority("ROLE_" + Role.ADMIN.name()) // "/admin/〜"は ADMIN ユーザのみアクセス可（認可設定）
                .anyRequest().authenticated(); // それ以外は直リンクNG（anyRequest ⇒ 全てのパス）

        // ログイン処理の設定
        http.formLogin() // 
                // .loginProcessingUrl("/login") // ログイン処理のリクエスト先 （ログイン処理のPOST先URL。ログインボタン等のth:actionで指定するパス）
                .loginPage("/login") // ログインページのパス（ログイン画面のGETリクエスト先。未指定時、Spring Security標準のログイン画面が使用される）
                // .failureUrl("/login?error") // ログイン失敗時の遷移先
                // .usernameParameter("userId") // ログインページのユーザーIDのname属性
                // .passwordParameter("password") // ログインページのパスワードのname属性
                .defaultSuccessUrl("/"); // 認証後にリダイレクトするパス

        // ログアウト処理の設定
        // Spring Securityがログアウト処理を行ってくれるためログアウト用のコントローラは不要
        http.logout() // 
                // .logoutUrl("/logout") // ログアウト処理のリクエスト先（POSTメソッド用）※Spring Securityのデフォルトログアウト処理はPOSTメソッドで送る
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")); // ログアウト処理のリクエスト先（GETメソッド用）※GETメソッドでログアウトする場合はこの設定を使用する
        //      .logoutSuccessUrl("/login?logout"); // ログアウト成功時にリダイレクトするパス

        // Remember-Meの設定
        // これを設定すると、ブラウザを閉じて再度開いた場合でもログインしたままにできる
        http.rememberMe();

        // Remember-Meは標準で14日間有効になる
        // 有効期限を変更する場合以下のようにする
        // http.rememberMe() //
        //         .tokenValiditySeconds(86400); // 保存期間を1日(86400秒)にする

    }

    /**
     * ログイン認証の設定
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // インメモリ認証
        //
        // 仮のユーザーIDとパスワードを用意してログインできるようにする（インメモリ認証）。
        // ユーザマスタ等（DB）が使えない場合やテストユーザを用意するのに有効。
        // 
        // ユーザ名「admin」と「user」を用意する。パスワードは両方とも「pass」。
        auth.inMemoryAuthentication() //
                .withUser("admin") // ユーザを追加
                .password(passwordEncoder().encode("pass")) // パスワード設定（Spring Securityではパスワードの暗号化必須）
                // .roles("GENERAL") // ロール設定（"ROLE_"は省略可）
                .authorities("ROLE_ADMIN") // 権限設定（こちらは"ROLE_"を付ける必要あり）
                .and() //
                .withUser("user") // ユーザを追加
                .password(passwordEncoder().encode("pass")) // パスワード設定（Spring Securityではパスワードの暗号化必須）
                // .roles("ADMIN"); // ロール設定（"ROLE_"は省略可）
                .authorities("ROLE_USER"); // 権限設定（こちらは"ROLE_"を付ける必要あり）

        // DBのユーザで認証
        // userDetailsServiceを使用してDBユーザを参照できるようにする
        auth.userDetailsService(userDetailsService) // 
                .passwordEncoder(passwordEncoder()); // パスワード暗号化用エンコーダの指定
    }

}
