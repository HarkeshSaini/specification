package com.specification.service.request.function;

import com.specification.service.domain.BlogStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogUpdateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String slug;
    private String summary;
    private String content;
    private String category;
    private List<String> tags;
    private String featuredImageUrl;
    private BlogStatus status;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Boolean allowComments;
}
