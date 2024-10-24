package com.example.dpm.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.dpm.auth.enums.MemberRole;
import com.example.dpm.auth.filter.JwtFilter;
import com.example.dpm.auth.service.JwtTokenService;
import com.example.dpm.exception.ExceptionHandlerFilter;
import com.example.dpm.member.service.MemberService;

import static org.springframework.security.config.Customizer.withDefaults;

//com.epguy.auth.config
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login/**", "/token/refresh", "/post/**", "/mypage/**").permitAll()
                        .requestMatchers("/user/**").hasAuthority(MemberRole.USER.getRole())
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable()) // 로그인 폼 미사용
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()) // http basic 미사용
                .addFilterBefore(new JwtFilter(jwtTokenService, memberService), UsernamePasswordAuthenticationFilter.class) // JWT Filter 추가
                .addFilterBefore(new ExceptionHandlerFilter(), JwtFilter.class) // Security Filter 에서 CustomException 사용하기 위해 추가
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // 아래 url은 filter 에서 제외
        return web ->
            web.ignoring()
                    .requestMatchers("/login/**", "/token/refresh", "/post/**", "/mypage/**");
    }
}
