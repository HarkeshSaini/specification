package com.specification.service.response.profile;

import com.specification.service.domain.entity.user.UserAddressDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUserProfileResponse {

    private String id;
    private String email;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String profileImageUrl;
    private Boolean active;
    private Instant lastLoginAt;
    private UserAddressDetail address;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
