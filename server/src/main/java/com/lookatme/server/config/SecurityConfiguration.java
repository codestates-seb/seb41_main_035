package com.lookatme.server.config;

import com.lookatme.server.auth.filter.JwtAuthenticationFilter;
import com.lookatme.server.auth.filter.JwtVerificationFilter;
import com.lookatme.server.auth.handler.*;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {

    private final JwtTokenizer jwtTokenizer;
    private final MemberAuthorityUtils authorityUtils;
    private final RedisRepository redisRepository;
    private final MemberRepository memberRepository;
    private final LoginTransactionalListener loginTransactionalListener;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin() // H2 콘솔을 위한 설정
                .and()
                .csrf().disable() // csrf 토큰 검증 X
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 X
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.GET, "/auth/profile").permitAll()
                        .antMatchers(HttpMethod.POST, "/members/signup").permitAll()
                        .antMatchers(HttpMethod.GET, "/members/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/comment/**").permitAll()
                        .antMatchers( "/auth/**").authenticated()
                        .antMatchers("/comment/**").authenticated()
                        .antMatchers("/members/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OauthAuthenticationSuccessHandler(jwtTokenizer, memberRepository))
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowedHeaders(List.of("*")); // 허용할 헤더 추가
        configuration.setAllowedOriginPatterns(List.of("*")); // 허용할 도메인
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS")); // 허용할 메서드
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, redisRepository, jwtTokenizer);
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler(loginTransactionalListener));
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler(loginTransactionalListener));
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(redisRepository, jwtTokenizer, authorityUtils);

            builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class); // Oauth필터 뒤에 JwtVerifiaction 필터 배치
            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class)
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
