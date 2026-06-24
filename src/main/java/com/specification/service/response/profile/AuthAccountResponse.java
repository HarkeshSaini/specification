package com.specification.service.response.profile;

import com.specification.service.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Auth account read model. Sensitive fields (password hash, tokens) are never exposed.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthAccountResponse {

    private String id;
    private String email;
    private String username;
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
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
