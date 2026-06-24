package com.specification.service.domain.entity.user;

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
@Document(collection = "manager_users")
public class ManagerUserDetail extends BaseUserProfile {

    private String managedRegion;
    private List<String> managedDepartments;
    private List<String> managedTeams;
    private String designation;
    private String department;
    private String reportingDirector;
    private Map<String, Object> additionalAttributes;
}
