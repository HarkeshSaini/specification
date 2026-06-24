package com.specification.service.config;

import com.specification.service.domain.repository.security.AuthAccountRepository;
import com.specification.service.domain.repository.security.SuperAdminUserRepository;
import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.service.user.superadmin.ISuperAdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * Seeds one default Super Admin when no super admin profile exists (first run / empty DB).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SuperAdminBootstrapInitializer {

    private static final String SYSTEM_ACTOR = "system-bootstrap";

    private final SuperAdminBootstrapProperties properties;
    private final SuperAdminUserRepository superAdminUserRepository;
    private final AuthAccountRepository authAccountRepository;
    private final ISuperAdminUserService superAdminUserService;

    @EventListener(ApplicationReadyEvent.class)
    public void seedSuperAdminIfMissing() {
        if (!properties.isEnabled()) {
            log.info("Super admin bootstrap is disabled");
            return;
        }
        if (!StringUtils.hasText(properties.getPassword())) {
            log.warn("Super admin bootstrap skipped: bootstrap.super-admin.password is empty");
            return;
        }

        superAdminUserRepository.countByDeletedFalse()
                .flatMap(count -> count > 0 ? Mono.empty() : createDefaultSuperAdmin())
                .doOnSuccess(ignored -> log.debug("Super admin bootstrap finished"))
                .doOnError(ex -> log.error("Super admin bootstrap failed: {}", ex.getMessage()))
                .subscribe();
    }

    private Mono<Void> createDefaultSuperAdmin() {
        String email = properties.getEmail().trim().toLowerCase();
        return authAccountRepository.existsByEmail(email)
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        log.info("Super admin bootstrap skipped: auth account already exists for {}", email);
                        return Mono.empty();
                    }
                    SuperAdminProfileRequest request = SuperAdminProfileRequest.builder()
                            .email(email)
                            .username(properties.getUsername())
                            .password(properties.getPassword())
                            .fullName(properties.getFullName())
                            .department(properties.getDepartment())
                            .designation(properties.getDesignation())
                            .allAccess(true)
                            .active(true)
                            .roles(java.util.List.of("SUPER_ADMIN"))
                            .permissions(java.util.List.of("ALL"))
                            .build();
                    return superAdminUserService.create(request, SYSTEM_ACTOR)
                            .doOnSuccess(profile -> log.warn(
                                    "Default Super Admin created — email: {}, username: {} (change password after first login)",
                                    profile.getEmail(),
                                    properties.getUsername()))
                            .then();
                });
    }
}
