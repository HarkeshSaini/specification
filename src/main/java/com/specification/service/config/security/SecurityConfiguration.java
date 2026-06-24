package com.specification.service.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.config.security.jwtconfig.JwtAuthenticationWebFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtAuthenticationWebFilter jwtAuthenticationWebFilter) {
		log.debug(ApplicationConstant.SECURITY_WEB_FILTER_CHAIN_CONFIGURED);
		return http.csrf(ServerHttpSecurity.CsrfSpec::disable).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable).addFilterBefore(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.authorizeExchange(exchange -> exchange.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.pathMatchers(HttpMethod.GET, "/", "/index.html", "/favicon.ico").permitAll()
						.pathMatchers(HttpMethod.GET, "/login", "/blog", "/blog/**", "/ai", "/ai/chat", "/contact").permitAll()
						.pathMatchers(HttpMethod.GET, "/admin/**", "/manager/**", "/user/**").permitAll()
						.pathMatchers(HttpMethod.GET, "/assets/**").permitAll()
						.pathMatchers("/api/auth/**").permitAll()
						.pathMatchers(HttpMethod.GET, "/api/public/blog/**").permitAll()
						.pathMatchers("/api/super-admin/**").hasRole("SUPER_ADMIN")
						.pathMatchers("/api/admin/**").hasAnyRole("ADMIN")
						.pathMatchers("/api/manager/**").hasAnyRole("MANAGER")
						.pathMatchers("/api/user/**").hasRole("WEB_USER")
						.anyExchange().authenticated())
				.build();
	}
}
