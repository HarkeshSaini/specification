package com.specification.service.response.profile;

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
public class ManagerProfileResponse extends BaseUserProfileResponse {

    private String managedRegion;
    private List<String> managedDepartments;
    private List<String> managedTeams;
    private String designation;
    private String department;
    private String reportingDirector;
    private Map<String, Object> additionalAttributes;
}
