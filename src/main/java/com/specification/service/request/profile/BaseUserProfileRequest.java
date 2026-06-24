package com.specification.service.request.profile;

import com.specification.service.domain.entity.user.UserAddressDetail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Shared profile fields for create/update requests. Credentials are sent via {@code password}
 * and stored in {@link com.specification.service.domain.entity.user.AuthAccountDetail}.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUserProfileRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String username;
    private String fullName;
    private String phoneNumber;
    private String profileImageUrl;
    private Boolean active;

    /** Required on create; optional on update. Stored in auth account only. */
    private String password;

    private UserAddressDetail address;
}
