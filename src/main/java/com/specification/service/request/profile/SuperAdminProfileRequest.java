package com.specification.service.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SuperAdminProfileRequest extends BaseUserProfileRequest {

    private List<String> roles;
    private List<String> permissions;
    private Boolean allAccess;
    private String department;
    private String designation;
    private String reportingManager;
    private String gender;
    private Instant dateOfBirth;
    private Map<String, Object> additionalAttributes;
}
