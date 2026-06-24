package com.specification.service.config.security.jwtconfig;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.specification.service.constant.ApplicationConstant;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Bearer-token JWT filter: runs before Spring Security authorization and attaches the
 * {@link org.springframework.security.core.context.SecurityContext} for reactive calls.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationWebFilter implements WebFilter {

	private final JwtService jwtService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getPath().value();
		if (path.startsWith("/api/auth")) {
			log.trace(ApplicationConstant.JWT_SKIP_PROCESSING_FOR_PATH, path);
			return chain.filter(exchange);
		}
		String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(header) || !header.startsWith(ApplicationConstant.AUTHORIZATION_BEARER_PREFIX)) {
			return chain.filter(exchange);
		}
		String token = header.substring(ApplicationConstant.AUTHORIZATION_BEARER_PREFIX_LENGTH).trim();
		if (!StringUtils.hasText(token)) {
			return chain.filter(exchange);
		}
		return jwtService.parseAuthentication(token).flatMap(auth -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))).onErrorResume(JwtException.class, e -> {
			log.warn(ApplicationConstant.JWT_REJECTED_FOR_PATH, path, e.getMessage());
			return unauthorized(exchange);
		});
	}

	private Mono<Void> unauthorized(ServerWebExchange exchange) {
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		log.debug(ApplicationConstant.RETURNING_UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}
}
