package com.specification.service.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebsiteProfileRequest extends BaseUserProfileRequest {

    private String language;
    private String timezone;
    private String theme;
    private Map<String, Object> preferences;
}
