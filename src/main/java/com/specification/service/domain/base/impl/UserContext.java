package com.specification.service.domain.base.impl;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.Objects;
import java.util.UUID;

import com.specification.service.domain.base.ContextProvider;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private final ContextProvider userContextProvider;

    public UserContext(ContextProvider userContextProvider) {
        this.userContextProvider = Objects.requireNonNull(userContextProvider, "userContextProvider must not be null");
    }

    public UUID getCurrentTenant() {
        return this.userContextProvider.getCurrentTenant();
    }

    public String getAppVersion() {
        return this.userContextProvider.getAppVersion();
    }

    public String getChannelId() {
        return this.userContextProvider.getChannelId();
    }

    public String getMarketId() {
        return this.userContextProvider.getMarketId();
    }

    public String getKeycloakUserId() {
        return this.userContextProvider.getKeycloakUserId();
    }
}
