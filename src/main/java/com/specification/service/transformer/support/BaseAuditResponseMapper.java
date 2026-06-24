package com.specification.service.transformer.support;

import com.specification.service.domain.base.impl.BaseEntity;

import java.time.Instant;

/**
 * Maps shared {@link BaseEntity} audit fields into profile response DTOs.
 */
public final class BaseAuditResponseMapper {

    private BaseAuditResponseMapper() {
    }

    public record AuditFields(
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy) {
    }

    public static AuditFields from(BaseEntity entity) {
        if (entity == null) {
            return new AuditFields(null, null, null, null);
        }
        return new AuditFields(
                entity.getCreatedTimestamp(),
                entity.getUpdateTimestamp(),
                entity.getCreatedBy(),
                entity.getUpdatedBy());
    }
}
