package com.specification.service.transformer.support;

import org.springframework.util.StringUtils;

public final class UserRequestValidator {

    private UserRequestValidator() {
    }

    public static void requirePasswordOnCreate(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password is required when creating a user");
        }
    }
}
