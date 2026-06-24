package com.specification.service.domain.base.impl;

import com.specification.service.domain.base.ContextProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserContextProvider implements ContextProvider {


    @Override
    public UUID getCurrentTenant() {
        return UUID.randomUUID();
    }

    @Override
    public String getAppVersion() {
        return String.valueOf(UUID.randomUUID().version());
    }

    @Override
    public String getChannelId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getMarketId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getKeycloakUserId() {
        return UUID.randomUUID().toString();
    }
}
