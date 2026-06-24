package com.specification.service.domain.base.impl;

import java.time.Instant;

import com.specification.service.domain.entity.user.AuthAccountDetail;
import com.specification.service.domain.entity.user.UserAddressDetail;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Shared profile fields for all user types. Credentials live in {@link AuthAccountDetail} only.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseUserProfile extends BaseEntity {

    @Indexed(unique = true)
    private String email;

    private String username;
    private String fullName;
    private String phoneNumber;
    private String profileImageUrl;
    private Boolean active;
    private Instant lastLoginAt;
    private UserAddressDetail address;
}
