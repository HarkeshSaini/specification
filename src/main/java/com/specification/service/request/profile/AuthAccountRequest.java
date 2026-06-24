package com.specification.service.request.profile;

import com.specification.service.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Auth account create/update payload. Password is write-only; never persisted on profile entities.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthAccountRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String username;

    /** Required on create; optional on update. */
    private String password;

    private Boolean enabled;
    private Boolean accountLocked;
    private Boolean passwordExpired;
    private UserRole role;
    private List<String> authorities;
    private String profileId;
    private Boolean emailVerified;
}
