package com.taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Configuration
public class TestSecurityConfig {

    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                if (targetDomainObject instanceof String && "Task".equals(targetDomainObject) && "VIEW".equals(permission)) {
                    return true; // Grant VIEW permission for Task objects in tests
                }
                return false;
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                if ("Task".equals(targetType) && "VIEW".equals(permission)) {
                    return true; // Grant VIEW permission for Task objects by ID in tests
                }
                return false;
            }
        };
    }
}
