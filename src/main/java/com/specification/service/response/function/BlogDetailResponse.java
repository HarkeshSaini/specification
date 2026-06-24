package com.specification.service.response.function;

import com.specification.service.domain.BlogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetailResponse {

    private String id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String authorId;
    private String authorName;
    private String category;
    private List<String> tags;
    private String featuredImageUrl;
    private BlogStatus status;
    private boolean published;
    private LocalDateTime publishedAt;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private long views;
    private long likes;
    private long shares;
    private boolean allowComments;
    private Instant createdTimestamp;
    private Instant updateTimestamp;
}
