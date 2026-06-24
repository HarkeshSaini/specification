package com.specification.service.service.function.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.BlogStatus;
import com.specification.service.domain.entity.function.BlogDetails;
import com.specification.service.domain.repository.function.BlogDetailsRepository;
import com.specification.service.exception.ConflictException;
import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.function.IBlogService;
import com.specification.service.transformer.function.IBlogTransformer;
import com.specification.service.transformer.support.ReactivePageSupport;
import com.specification.service.utility.ReactiveEntityGuards;
import com.specification.service.utility.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements IBlogService {

    private final BlogDetailsRepository blogDetailsRepository;
    private final IBlogTransformer blogTransformer;

    @Override
    public Mono<BlogDetailResponse> create(BlogCreateRequest request, String actor, String authorId, String authorName) {
        Instant now = Instant.now();
        return resolveUniqueSlug(request.getSlug(), request.getTitle(), null)
                .flatMap(slug -> {
                    BlogDetails draft = blogTransformer.toNewEntity(request, slug, actor, now, authorId, authorName);
                    return blogDetailsRepository.save(draft);
                })
                .map(blogTransformer::toDetailResponse);
    }

    @Override
    public Mono<BlogDetailResponse> update(String id, BlogUpdateRequest request, String actor) {
        Instant now = Instant.now();
        return ReactiveEntityGuards.orNotFound(blogDetailsRepository.findByIdAndDeletedFalse(id), ApplicationConstant.BLOG_LABEL, id)
                .flatMap(blog -> resolveUniqueSlug(request.getSlug(), request.getTitle(), blog.getId())
                        .flatMap(slug -> {
                            blogTransformer.applyUpdates(blog, request, slug, actor, now);
                            return blogDetailsRepository.save(blog);
                        }))
                .map(blogTransformer::toDetailResponse);
    }

    @Override
    public Mono<BlogDetailResponse> getById(String id) {
        return ReactiveEntityGuards.orNotFound(blogDetailsRepository.findByIdAndDeletedFalse(id), ApplicationConstant.BLOG_LABEL, id)
                .map(blogTransformer::toDetailResponse);
    }

    @Override
    public Mono<BlogDetailResponse> getPublishedBySlug(String slug) {
        return ReactiveEntityGuards.orNotFound(
                        blogDetailsRepository.findBySlugAndDeletedFalse(slug)
                                .filter(b -> b.getStatus() == BlogStatus.PUBLISHED),
                        ApplicationConstant.BLOG_LABEL,
                        slug)
                .flatMap(blog -> {
                    blog.setViews(blog.getViews() + 1);
                    return blogDetailsRepository.save(blog);
                })
                .map(blogTransformer::toDetailResponse);
    }

    @Override
    public Mono<PageResponse<BlogSummaryResponse>> listPublished(int page, int size) {
        ReactivePageSupport.validatePage(page, size);
        long offset = (long) page * size;
        return ReactivePageSupport.collect(
                blogDetailsRepository.findByStatusAndDeletedFalseOrderByPublishedAtDesc(BlogStatus.PUBLISHED)
                        .skip(offset)
                        .take(size)
                        .map(blogTransformer::toSummaryResponse),
                blogDetailsRepository.countByStatusAndDeletedFalse(BlogStatus.PUBLISHED),
                page,
                size);
    }

    @Override
    public Mono<PageResponse<BlogSummaryResponse>> listAll(int page, int size) {
        ReactivePageSupport.validatePage(page, size);
        long offset = (long) page * size;
        return ReactivePageSupport.collect(
                blogDetailsRepository.findAllByDeletedFalseOrderByUpdateTimestampDesc()
                        .skip(offset)
                        .take(size)
                        .map(blogTransformer::toSummaryResponse),
                blogDetailsRepository.countByDeletedFalse(),
                page,
                size);
    }

    @Override
    public Mono<Void> softDelete(String id, String actor) {
        Instant now = Instant.now();
        return ReactiveEntityGuards.orNotFound(blogDetailsRepository.findByIdAndDeletedFalse(id), ApplicationConstant.BLOG_LABEL, id)
                .flatMap(blog -> {
                    blog.setDeleted(true);
                    blog.setDeletedAt(now);
                    blog.setDeletedBy(actor);
                    blog.setUpdateTimestamp(now);
                    blog.setUpdatedBy(actor);
                    return blogDetailsRepository.save(blog);
                })
                .then();
    }

    private Mono<String> resolveUniqueSlug(String requestedSlug, String title, String excludeId) {
        String base = StringUtils.hasText(requestedSlug) ? SlugUtils.toSlug(requestedSlug) : SlugUtils.toSlug(title);
        if (!StringUtils.hasText(base)) {
            return Mono.error(new ConflictException("Unable to generate slug from title"));
        }
        return ensureUniqueSlug(base, excludeId, 0);
    }

    private Mono<String> ensureUniqueSlug(String candidate, String excludeId, int suffix) {
        String slug = suffix == 0 ? candidate : candidate + "-" + suffix;
        return blogDetailsRepository.existsBySlugAndDeletedFalse(slug)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.just(slug);
                    }
                    if (excludeId != null) {
                        return blogDetailsRepository.findBySlugAndDeletedFalse(slug)
                                .flatMap(existing -> excludeId.equals(existing.getId())
                                        ? Mono.just(slug)
                                        : ensureUniqueSlug(candidate, excludeId, suffix + 1));
                    }
                    return ensureUniqueSlug(candidate, excludeId, suffix + 1);
                });
    }
}
