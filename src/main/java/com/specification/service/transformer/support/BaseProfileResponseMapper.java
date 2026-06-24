package com.specification.service.transformer.support;

import com.specification.service.domain.base.impl.BaseUserProfile;
import com.specification.service.response.profile.BaseUserProfileResponse;

/**
 * Maps shared {@link BaseUserProfile} fields into profile response DTOs.
 */
public final class BaseProfileResponseMapper {

    private BaseProfileResponseMapper() {
    }

    public static void applyBaseFields(BaseUserProfile entity, BaseUserProfileResponse.BaseUserProfileResponseBuilder<?, ?> builder) {
        if (entity == null || builder == null) {
            return;
        }
        BaseAuditResponseMapper.AuditFields audit = BaseAuditResponseMapper.from(entity);
        builder
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .fullName(entity.getFullName())
                .phoneNumber(entity.getPhoneNumber())
                .profileImageUrl(entity.getProfileImageUrl())
                .active(entity.getActive())
                .lastLoginAt(entity.getLastLoginAt())
                .address(entity.getAddress())
                .createdAt(audit.createdAt())
                .updatedAt(audit.updatedAt())
                .createdBy(audit.createdBy())
                .updatedBy(audit.updatedBy());
    }
}
