package com.specification.service.domain.base.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseEntity implements Serializable {

    @Id
    private String id;

    @CreatedDate
    @Field("created_at")
    private Instant createdTimestamp;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updateTimestamp;

    @LastModifiedBy
    @Field("updated_by")
    private String updatedBy;

    @Field("is_deleted")
    private boolean deleted = false;

    @Field("deleted_at")
    private Instant deletedAt;

    @Field("deleted_by")
    private String deletedBy;

    @Field("channel_id")
    private String channelId;

    @Field("created_by_app_version")
    private String createdByAppVersion;

    @Field("updated_by_app_version")
    private String updatedByAppVersion;
}
