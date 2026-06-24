package com.specification.service.domain.entity.function;

import com.specification.service.domain.base.impl.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity {

    private String blogId;
    private String userId;
    private String comment;
    private LocalDateTime createdAt;
}
