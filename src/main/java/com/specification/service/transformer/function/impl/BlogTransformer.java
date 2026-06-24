package com.specification.service.transformer.function.impl;

import com.specification.service.domain.BlogStatus;
import com.specification.service.domain.entity.function.BlogDetails;
import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.transformer.function.IBlogTransformer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class BlogTransformer implements IBlogTransformer {

    @Override
    public BlogDetails toNewEntity(BlogCreateRequest request, String slug, String actor, Instant now, String authorId, String authorName) {
        BlogStatus status = resolveStatus(request.getStatus());
        boolean published = status == BlogStatus.PUBLISHED;
        return BlogDetails.builder()
                .title(request.getTitle().trim())
                .slug(slug)
                .summary(trimOrNull(request.getSummary()))
                .content(trimOrNull(request.getContent()))
                .authorId(authorId)
                .authorName(authorName)
                .category(trimOrNull(request.getCategory()))
                .tags(request.getTags())
                .featuredImageUrl(trimOrNull(request.getFeaturedImageUrl()))
                .status(status)
                .isPublished(published)
                .publishedAt(published ? LocalDateTime.now() : null)
                .metaTitle(trimOrNull(request.getMetaTitle()))
                .metaDescription(trimOrNull(request.getMetaDescription()))
                .metaKeywords(trimOrNull(request.getMetaKeywords()))
                .allowComments(request.getAllowComments() == null || request.getAllowComments())
                .views(0L)
                .likes(0L)
                .shares(0L)
                .deleted(false)
                .createdTimestamp(now)
                .updateTimestamp(now)
                .createdBy(actor)
                .updatedBy(actor)
                .build();
    }

    @Override
    public void applyUpdates(BlogDetails blog, BlogUpdateRequest request, String slug, String actor, Instant now) {
        BlogStatus status = resolveStatus(request.getStatus());
        boolean wasPublished = blog.getStatus() == BlogStatus.PUBLISHED;
        boolean nowPublished = status == BlogStatus.PUBLISHED;

        blog.setTitle(request.getTitle().trim());
        blog.setSlug(slug);
        blog.setSummary(trimOrNull(request.getSummary()));
        blog.setContent(trimOrNull(request.getContent()));
        blog.setCategory(trimOrNull(request.getCategory()));
        blog.setTags(request.getTags());
        blog.setFeaturedImageUrl(trimOrNull(request.getFeaturedImageUrl()));
        blog.setStatus(status);
        blog.setPublished(nowPublished);
        if (nowPublished && !wasPublished && blog.getPublishedAt() == null) {
            blog.setPublishedAt(LocalDateTime.now());
        }
        blog.setMetaTitle(trimOrNull(request.getMetaTitle()));
        blog.setMetaDescription(trimOrNull(request.getMetaDescription()));
        blog.setMetaKeywords(trimOrNull(request.getMetaKeywords()));
        if (request.getAllowComments() != null) {
            blog.setAllowComments(request.getAllowComments());
        }
        blog.setUpdatedBy(actor);
        blog.setUpdateTimestamp(now);
    }

    @Override
    public BlogSummaryResponse toSummaryResponse(BlogDetails blog) {
        return BlogSummaryResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .slug(blog.getSlug())
                .summary(blog.getSummary())
                .authorName(blog.getAuthorName())
                .category(blog.getCategory())
                .tags(blog.getTags())
                .featuredImageUrl(blog.getFeaturedImageUrl())
                .status(blog.getStatus())
                .publishedAt(blog.getPublishedAt())
                .views(blog.getViews())
                .createdTimestamp(blog.getCreatedTimestamp())
                .updateTimestamp(blog.getUpdateTimestamp())
                .build();
    }

    @Override
    public BlogDetailResponse toDetailResponse(BlogDetails blog) {
        return BlogDetailResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .slug(blog.getSlug())
                .summary(blog.getSummary())
                .content(blog.getContent())
                .authorId(blog.getAuthorId())
                .authorName(blog.getAuthorName())
                .category(blog.getCategory())
                .tags(blog.getTags())
                .featuredImageUrl(blog.getFeaturedImageUrl())
                .status(blog.getStatus())
                .published(blog.isPublished())
                .publishedAt(blog.getPublishedAt())
                .metaTitle(blog.getMetaTitle())
                .metaDescription(blog.getMetaDescription())
                .metaKeywords(blog.getMetaKeywords())
                .views(blog.getViews())
                .likes(blog.getLikes())
                .shares(blog.getShares())
                .allowComments(blog.isAllowComments())
                .createdTimestamp(blog.getCreatedTimestamp())
                .updateTimestamp(blog.getUpdateTimestamp())
                .build();
    }

    private static BlogStatus resolveStatus(BlogStatus status) {
        return status == null ? BlogStatus.DRAFT : status;
    }

    private static String trimOrNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
