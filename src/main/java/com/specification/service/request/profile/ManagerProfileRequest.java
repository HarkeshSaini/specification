package com.specification.service.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManagerProfileRequest extends BaseUserProfileRequest {

    private String managedRegion;
    private List<String> managedDepartments;
    private List<String> managedTeams;
    private String designation;
    private String department;
    private String reportingDirector;
    private Map<String, Object> additionalAttributes;
}
