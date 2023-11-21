package com.chunjae.edumarket.config;

import com.chunjae.edumarket.biz.UserService;
import com.chunjae.edumarket.biz.UserServiceImpl;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
//스프링시크리티 설정 관리자
@Configuration
public class SecurityConfig {

    @Bean
    public UserService userService() { return new UserServiceImpl();  }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 권한에 따라 허용하는 url 설정
        // .antMatchers는 /login, /join 페이지는 모두 허용, 다른 페이지는 인증된 사용자만 허용
        // 자원의 경로는 mvcMatchers 로
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/common/**", "/food/**").permitAll()
                .antMatchers("/notice/noticeList", "/notice/noticeDetail").permitAll()
                .antMatchers("/faq/**").permitAll()
                .antMatchers("/free/freeList").permitAll()// "/login", "/join", "/idCheck", "/emailCheck", "/joinPro", "/updateForm", "/updateUserPro"
                .antMatchers("/product/**").hasAnyAuthority("ADMIN", "EMP", "USER")
                .antMatchers("/user/**").hasAnyAuthority("ADMIN", "EMP", "USER")
                .antMatchers("/board/**").hasAnyAuthority("ADMIN", "EMP", "USER")
                .antMatchers("/chat/**").hasAnyAuthority("ADMIN", "EMP", "USER")
                .antMatchers("/admin/**").hasAuthority("ADMIN") // Role 말고 Authority로 적용
                .antMatchers("/emp/**").hasAnyAuthority("ADMIN", "EMP")
                .mvcMatchers("/css/**", "/js/**", "/img/**", "/cleditor/**", "/scss/**", "/vendors/**", "/ckeditor/**", "/webfonts/**").permitAll()
                .anyRequest().authenticated(); // 앞에서 선언한 모든 것들을 인증
                // "/webfonts/**" 웹 폰트를 추가하는 경우 이거 넣어주자, 안하면 error page 뜸

        // login 설정
        http
                .formLogin()
                .loginPage("/common/login")    // GET 요청 (login form을 보여줌)
                .loginProcessingUrl("/auth")    // POST 요청 (login 창에 입력한 데이터를 처리)
                .usernameParameter("name")	// login에 필요한 id 값을 email로 설정 (default는 username)
                .passwordParameter("password")	// login에 필요한 password 값을 password(default)로 설정
                .defaultSuccessUrl("/");	// login에 성공하면 /로 redirect

        // logout 설정
        http
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/");	// logout에 성공하면 /로 redirect

        //cors, csrf 방지 해제
//        http.csrf().disable();
        http.cors().disable(); // 이거 방지 안해주면 이미지 안뜸

        //중복 로그인 방지
        http.sessionManagement()
                .sessionFixation().changeSessionId()
                .maximumSessions(1)
                .expiredSessionStrategy(new CustomSessionExpiredStrategy())
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
}
