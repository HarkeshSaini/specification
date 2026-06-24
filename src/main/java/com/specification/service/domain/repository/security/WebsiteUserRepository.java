package com.specification.service.domain.repository.security;

import com.specification.service.domain.entity.user.WebUserDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WebsiteUserRepository extends ReactiveMongoRepository<WebUserDetail, String> {
	Mono<WebUserDetail> findByIdAndDeletedFalse(String id);
	Flux<WebUserDetail> findAllByDeletedFalse();
	Mono<Long> countByDeletedFalse();
}
