package com.specification.service.domain.base.impl;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class BaseEntityListener extends AbstractMongoEventListener<BaseEntity> {

    private static final Logger log = LoggerFactory.getLogger(BaseEntityListener.class);

    private final UserContext userContext;

    public BaseEntityListener(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseEntity> event) {
        BaseEntity entity = event.getSource();

        Instant now = Instant.now();

        // Detect new entity (insert case)
        if (entity.getId() == null) {
            entity.setCreatedTimestamp(now);
            entity.setCreatedByAppVersion(userContext.getAppVersion());

            if (userContext.getKeycloakUserId() != null) {
                entity.setCreatedBy(userContext.getKeycloakUserId());
            }
        }

        // Common for both insert & update
        entity.setUpdateTimestamp(now);
        entity.setUpdatedByAppVersion(userContext.getAppVersion());
        entity.setChannelId(userContext.getChannelId());

        if (userContext.getKeycloakUserId() != null) {
            entity.setUpdatedBy(userContext.getKeycloakUserId());
        }

        // Soft delete logic (same as @PreUpdate)
        if (entity.isDeleted() && entity.getDeletedAt() == null) {
            entity.setDeletedAt(now);
        }
    }
}
