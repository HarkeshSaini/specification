package com.specification.service.domain.repository.security;

import com.specification.service.domain.entity.user.AdminUserDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminUserRepository extends ReactiveMongoRepository<AdminUserDetail, String> {

    Mono<AdminUserDetail> findByIdAndDeletedFalse(String id);
	Flux<AdminUserDetail> findAllByDeletedFalse();
	Mono<Long> countByDeletedFalse();
}
