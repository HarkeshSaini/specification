package com.specification.service.service.ollama.provider;

import com.specification.service.config.HuggingFaceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class HuggingFaceAvailabilityChecker {

    private static final Duration CHECK_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration CACHE_TTL = Duration.ofSeconds(30);

    private final WebClient webClient;
    private final HuggingFaceProperties properties;
    private final HuggingFaceUtility huggingFaceUtility;
    private final AtomicReference<CachedStatus> cache = new AtomicReference<>();

    public HuggingFaceAvailabilityChecker(
            WebClient.Builder webClientBuilder,
            HuggingFaceProperties properties,
            HuggingFaceUtility huggingFaceUtility) {
        this.webClient = webClientBuilder.build();
        this.properties = properties;
        this.huggingFaceUtility = huggingFaceUtility;
    }

    public boolean isAvailable() {
        if (!huggingFaceUtility.isConfigured()) {
            return false;
        }
        if (huggingFaceUtility.hasApiKey()) {
            return true;
        }
        CachedStatus current = cache.get();
        if (current != null && current.isValid()) {
            return current.available();
        }
        boolean available = pingFreeRouter();
        cache.set(new CachedStatus(available, Instant.now().plus(CACHE_TTL)));
        return available;
    }

    public void invalidateCache() {
        cache.set(null);
    }

    private boolean pingFreeRouter() {
        try {
            webClient.get()
                    .uri(properties.getFreeBaseUrl())
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(CHECK_TIMEOUT)
                    .block();
            log.debug("HuggingFace free router reachable at {}", properties.getFreeBaseUrl());
            return true;
        } catch (Exception ex) {
            log.debug("HuggingFace free router unreachable at {}: {}", properties.getFreeBaseUrl(), ex.getMessage());
            return false;
        }
    }

    private record CachedStatus(boolean available, Instant expiresAt) {
        boolean isValid() {
            return Instant.now().isBefore(expiresAt);
        }
    }
}
