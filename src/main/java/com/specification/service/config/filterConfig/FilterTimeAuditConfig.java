package com.specification.service.config.filterConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;

/**
 * Configuration class to register WebFlux filters properly.
 *
 * NOTE:
 * - FilterRegistrationBean is NOT used in WebFlux
 * - WebFilter beans are auto-registered by Spring
 */
@Configuration
public class FilterTimeAuditConfig {

    private static final int FILTER_ORDER = -1;

    @Bean
    @Order(FILTER_ORDER)
    public WebFilter calculateTimeResponseFilter() {
        return new CalculateTimeResponse();
    }
}