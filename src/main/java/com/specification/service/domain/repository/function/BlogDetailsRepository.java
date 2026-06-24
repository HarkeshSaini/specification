package com.specification.service.domain.repository.function;

import com.specification.service.domain.BlogStatus;
import com.specification.service.domain.entity.function.BlogDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlogDetailsRepository extends ReactiveMongoRepository<BlogDetails, String> {

    Mono<BlogDetails> findByIdAndDeletedFalse(String id);

    Mono<BlogDetails> findBySlugAndDeletedFalse(String slug);

    Mono<Boolean> existsBySlugAndDeletedFalse(String slug);

    Flux<BlogDetails> findByStatusAndDeletedFalseOrderByPublishedAtDesc(BlogStatus status);

    Mono<Long> countByStatusAndDeletedFalse(BlogStatus status);

    Flux<BlogDetails> findAllByDeletedFalseOrderByUpdateTimestampDesc();

    Mono<Long> countByDeletedFalse();
}
