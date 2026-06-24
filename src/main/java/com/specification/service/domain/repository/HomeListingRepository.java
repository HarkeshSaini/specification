package com.specification.service.domain.repository;

import com.specification.service.domain.entity.function.HomeListingKey;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeListingRepository extends ReactiveMongoRepository<HomeListingKey, String> {

    Optional<HomeListingKey> findByLanguageCode(String langCode);
}
