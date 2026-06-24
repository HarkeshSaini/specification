package com.specification.service.transformer.function;

import com.specification.service.domain.entity.function.BlogDetails;
import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;

import java.time.Instant;

public interface IBlogTransformer {

    BlogDetails toNewEntity(BlogCreateRequest request, String slug, String actor, Instant now, String authorId, String authorName);

    void applyUpdates(BlogDetails blog, BlogUpdateRequest request, String slug, String actor, Instant now);

    BlogSummaryResponse toSummaryResponse(BlogDetails blog);

    BlogDetailResponse toDetailResponse(BlogDetails blog);
}
