package com.specification.service.domain.repository.security;

import com.specification.service.domain.entity.user.SuperAdminUserDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SuperAdminUserRepository extends ReactiveMongoRepository<SuperAdminUserDetail, String> {

    Mono<SuperAdminUserDetail> findByIdAndDeletedFalse(String id);

    Flux<SuperAdminUserDetail> findAllByDeletedFalse();

    Mono<Long> countByDeletedFalse();
}
