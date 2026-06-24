package com.specification.service.response.function;

import com.specification.service.domain.BlogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/** Lightweight DTO for fast public/admin listing (no body content). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogSummaryResponse {

    private String id;
    private String title;
    private String slug;
    private String summary;
    private String authorName;
    private String category;
    private List<String> tags;
    private String featuredImageUrl;
    private BlogStatus status;
    private LocalDateTime publishedAt;
    private long views;
    private Instant createdTimestamp;
    private Instant updateTimestamp;
}
