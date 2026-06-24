package com.specification.service.domain.entity.user;

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
@Document(collection = "website_users")
public class WebUserDetail extends BaseUserProfile {

    private String language;
    private String timezone;
    private String theme;
    private Map<String, Object> preferences;
}
