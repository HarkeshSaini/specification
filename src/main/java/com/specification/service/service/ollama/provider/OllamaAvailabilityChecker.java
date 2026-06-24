package com.specification.service.service.ollama.provider;

import com.specification.service.config.OllamaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class OllamaAvailabilityChecker {

    private static final Duration CHECK_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration DEFAULT_CACHE_TTL = Duration.ofSeconds(30);

    private final WebClient webClient;
    private final OllamaProperties ollamaProperties;
    private final AtomicReference<CachedStatus> cache = new AtomicReference<>();

    public OllamaAvailabilityChecker(WebClient.Builder webClientBuilder, OllamaProperties ollamaProperties) {
        this.webClient = webClientBuilder.build();
        this.ollamaProperties = ollamaProperties;
    }

    public boolean isAvailable() {
        CachedStatus current = cache.get();
        if (current != null && current.isValid()) {
            return current.available();
        }
        boolean available = checkNow();
        cache.set(new CachedStatus(available, Instant.now().plus(DEFAULT_CACHE_TTL)));
        return available;
    }

    public void invalidateCache() {
        cache.set(null);
    }

    private boolean checkNow() {
        try {
            webClient.get().uri(ollamaProperties.getBaseUrl() + "/api/tags").retrieve().bodyToMono(String.class).timeout(CHECK_TIMEOUT).block();
            log.debug("Ollama is available at {}", ollamaProperties.getBaseUrl());
            return true;
        } catch (Exception ex) {
            log.debug("Ollama unavailable at {}: {}", ollamaProperties.getBaseUrl(), ex.getMessage());
            return false;
        }
    }

    private record CachedStatus(boolean available, Instant expiresAt) {
        boolean isValid() {
            return Instant.now().isBefore(expiresAt);
        }
    }
}
