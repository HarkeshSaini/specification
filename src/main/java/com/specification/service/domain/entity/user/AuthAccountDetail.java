package com.specification.service.domain.entity.user;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.specification.service.domain.UserRole;
import com.specification.service.domain.base.impl.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Login identity — one document per credential, linked to a profile via profileId + role.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "auth_accounts")
public class AuthAccountDetail extends BaseEntity {

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true, sparse = true)
    private String username;

    private String passwordHash;
    private Boolean enabled;
    private Boolean accountLocked;
    private Boolean passwordExpired;
    private UserRole role;
    private List<String> authorities;
    private String profileId;
    private Integer failedLoginAttempts;
    private Instant lastLoginAt;
    private Instant lastFailedLoginAt;
    private Instant credentialsUpdatedAt;
    private Boolean emailVerified;
    private String emailVerificationToken;
    private Instant emailVerificationExpiry;
    private String passwordResetToken;
    private Instant passwordResetExpiry;
}
