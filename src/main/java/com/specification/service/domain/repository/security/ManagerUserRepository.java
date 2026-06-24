package com.specification.service.domain.repository.security;

import com.specification.service.domain.entity.user.ManagerUserDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ManagerUserRepository extends ReactiveMongoRepository<ManagerUserDetail, String> {
	Mono<ManagerUserDetail> findByIdAndDeletedFalse(String id);
	Flux<ManagerUserDetail> findAllByDeletedFalse();
	Mono<Long> countByDeletedFalse();
}
