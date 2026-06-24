package com.specification.service.config.filterConfig;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Set;

@Component
@Order(-1)
public class CalculateTimeResponse implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateTimeResponse.class);

    private static final String LOG_FORMAT = "API LOG -> Method: {} | URI: {} | Status: {} | Time: {} | Client: {}";

    private static final Set<String> SKIP_URI_PREFIXES = Set.of("/actuator", "/health", "/swagger", "/v3/api-docs", "/webjars", "/favicon.ico");

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (shouldSkip(request.getPath().value())) {
            return chain.filter(exchange);
        }
        long startTime = System.currentTimeMillis();
        return chain.filter(exchange)
                .doFinally(signal -> {
                    long duration = System.currentTimeMillis() - startTime;
                    ServerHttpResponse response = exchange.getResponse();
                    int status = response.getStatusCode() != null  ? response.getStatusCode().value() : 200;
                    request.getMethod();
                    String method = request.getMethod().name();
                    String uri = request.getURI().toString();
                    String clientIp = resolveClientIp(request);
                    String formattedTime = formatDuration(duration);

//                    if (status >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                        LOGGER.error(LOG_FORMAT, method, uri, status, formattedTime, clientIp);
//                    } else if (status >= HttpStatus.BAD_REQUEST.value()) {
//                        LOGGER.warn(LOG_FORMAT, method, uri, status, formattedTime, clientIp);
//                    } else {
//                        LOGGER.info(LOG_FORMAT, method, uri, status, formattedTime, clientIp);
//                    }
                });
    }

    private boolean shouldSkip(String uri) {
        return uri != null && SKIP_URI_PREFIXES.stream().anyMatch(uri::startsWith);
    }

    private String resolveClientIp(ServerHttpRequest request) {
        String forwarded = request.getHeaders().getFirst("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            int comma = forwarded.indexOf(',');
            return (comma > 0) ? forwarded.substring(0, comma).trim()  : forwarded.trim();
        }

        InetSocketAddress address = request.getRemoteAddress();
        return address != null ? address.getAddress().getHostAddress() : "unknown";
    }

    private String formatDuration(long millis) {
        long hours = millis / 3600000;
        long minutes = (millis % 3600000) / 60000;
        long seconds = (millis % 60000) / 1000;
        long ms = millis % 1000;

        StringBuilder sb = new StringBuilder();

        if (hours > 0) sb.append(hours).append(" h ");
        if (minutes > 0) sb.append(minutes).append(" m ");
        if (seconds > 0) sb.append(seconds).append(" s ");
        if (ms > 0) sb.append(ms).append(" ms");

        return sb.isEmpty() ? "0 ms" : sb.toString().trim();
    }
}
