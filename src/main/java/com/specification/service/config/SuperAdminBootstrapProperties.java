package com.specification.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bootstrap.super-admin")
public class SuperAdminBootstrapProperties {

    private boolean enabled = true;
    private String email = "superadmin@specification.local";
    private String username = "superadmin";
    private String password = "SuperAdmin@2026!";
    private String fullName = "Platform Super Admin";
    private String department = "Platform";
    private String designation = "Super Administrator";
}
