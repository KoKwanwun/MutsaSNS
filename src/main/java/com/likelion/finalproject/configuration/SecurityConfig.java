package com.likelion.finalproject.configuration;

import com.likelion.finalproject.configuration.security.CustomAuthenticationEntryPoint;
import com.likelion.finalproject.configuration.security.JwtExceptionFilter;
import com.likelion.finalproject.configuration.security.JwtTokenFilter;
import com.likelion.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll() // join, login은 언제나 가능
                .antMatchers(HttpMethod.GET,"/api/v1/posts", "/api/v1/posts/{postId}", "/api/v1/posts/{postId}/comments", "/api/v1/posts/{postId}/likes").permitAll() // 조회는 회원, 비회원 모두 가능
                .antMatchers("/api/v1/**").authenticated() // 접근 요청 막기
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class) //UserNamePasswordAuthenticationFilter적용하기 전에 JWTTokenFilter를 적용 하라는 뜻 입니다.
                .addFilterBefore(new JwtExceptionFilter(), JwtTokenFilter.class)
                .build();
    }
}
