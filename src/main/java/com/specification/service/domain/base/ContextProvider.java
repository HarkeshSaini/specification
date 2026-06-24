package com.specification.service.domain.base;

import java.util.UUID;

public interface ContextProvider {
    UUID getCurrentTenant();
    String getAppVersion();
    String getChannelId();
    String getMarketId();
    String getKeycloakUserId();
}
