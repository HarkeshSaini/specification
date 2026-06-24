package com.specification.service.response.profile;

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
public class WebsiteProfileResponse extends BaseUserProfileResponse {

    private String language;
    private String timezone;
    private String theme;
    private Map<String, Object> preferences;
}
