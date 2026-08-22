package com.yongje.easyfit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.yongje.easyfit.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/", "/guide", "/login", "/css/**", "/js/**", "/oauth2/**").permitAll()
					.requestMatchers("/calendar", "/api/calendar/**").authenticated()
					.anyRequest().permitAll()
			)
			.oauth2Login(oauth2 -> oauth2
					.loginPage("/login")
					.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
					.defaultSuccessUrl("/", true)
			)
			.logout(logout -> logout
					.logoutSuccessUrl("/")
			);
		
		return http.build();
	}
}
