package com.specification.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class SpaFallbackConfiguration {

	private static final ClassPathResource INDEX = new ClassPathResource("static/index.html");

	@Bean
	public RouterFunction<ServerResponse> spaFallbackRouter() {
		return RouterFunctions.route()
				.GET("/login", req -> serveIndex())
				.GET("/blog", req -> serveIndex())
				.GET("/blog/**", req -> serveIndex())
				.GET("/ai", req -> serveIndex())
				.GET("/ai/**", req -> serveIndex())
				.GET("/contact", req -> serveIndex())
				.GET("/admin/**", req -> serveIndex())
				.GET("/manager/**", req -> serveIndex())
				.GET("/user/**", req -> serveIndex())
				.build();
	}

	private static Mono<ServerResponse> serveIndex() {
		return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(BodyInserters.fromResource(INDEX));
	}
}
