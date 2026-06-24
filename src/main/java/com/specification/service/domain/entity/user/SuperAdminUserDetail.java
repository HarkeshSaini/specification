package com.specification.service.domain.entity.user;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.specification.service.domain.base.impl.BaseUserProfile;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "super_admin_users")
public class SuperAdminUserDetail extends BaseUserProfile {

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
