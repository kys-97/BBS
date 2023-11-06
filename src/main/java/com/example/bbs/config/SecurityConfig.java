package com.example.bbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//스프링의 환경설정 파일임을 의미
//스프링 시큐리티 설정을 위해 사용
@Configuration
//모든 요청 url이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션
@EnableWebSecurity //내부적으로 SpringSecurityFilterChain이 동작하여 URL 필터가 적용
@EnableMethodSecurity(prePostEnabled = true) //@PreAuthorize 애너테이션을 사용하기 위해 반드시 필요
public class SecurityConfig {

    //스프링 시큐리티의 세부 설정은 SecurityFilterChain 빈을 생성하여 설정
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // 모든 인증되지 않은 요청을 허락한다는 의미 로그인을 하지 않더라도 모든 페이지에 접근
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/mysql/**")))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                //URL 요청시 X-Frame-Options 헤더값을 sameorigin으로 설정하여 오류가 발생하지 않도록
                                //X-Frame-Options 헤더의 값으로 sameorigin을 설정하면 frame에 포함된 페이지가 페이지를 제공하는 사이트와 동일한 경우에는 계속 사용
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin((formLogin) -> formLogin //login url
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    //비밀번호 암호화
    //BCryptPasswordEncoder는 BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호를 암호화
    @Bean
    PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //403 Forbidden 오류가 발생하는 이유는 스프링 시큐리티를 적용하면 CSRF 기능이 동작하기 때문
    //CSRF(cross site request forgery)는 웹 사이트 취약점 공격을 방지를 위해 사용하는 기술
    //스프링 시큐리티가 CSRF 토큰 값을 세션을 통해 발행하고 웹 페이지에서는 폼 전송시에 해당 토큰을 함께 전송하여 실제 웹 페이지에서 작성된 데이터가 전달되는지를 검증하는 기술


    @Bean //AuthenticationManager 빈을 생성
    // 스프링 시큐리티의 인증을 담당
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
