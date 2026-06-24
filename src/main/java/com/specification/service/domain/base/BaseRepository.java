package com.specification.service.domain.base;

import com.specification.service.domain.base.impl.BaseEntity;
import com.specification.service.exception.ErrorConstant;
import com.specification.service.exception.impl.ExceptionMessageUtil;
import com.specification.service.response.apires.ServiceException;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, I> extends ReactiveMongoRepository<T, I> {

    int DEFAULT_PAGE_MAX_SIZE = 100;

    default Mono<Void> deleteByIdSoft(I id) {
        return findById(id)
                .flatMap(entity -> {
                    entity.setDeleted(true);
                    entity.setDeletedAt(Instant.now());
                    return save(entity);
                })
                .then();
    }

    default Mono<T> deleteSoft(T entity) {
        entity.setDeleted(true);
        entity.setDeletedAt(Instant.now());
        return save(entity);
    }

    default Mono<Void> deleteAllByIdSoft(Iterable<I> ids) {
        return Flux.fromIterable(ids)
                .flatMap(this::deleteByIdSoft)
                .then();
    }

    default Flux<T> deleteAllSoft(Iterable<T> entities) {
        return Flux.fromIterable(entities)
                .flatMap(entity -> {
                    entity.setDeleted(true);
                    entity.setDeletedAt(Instant.now());
                    return save(entity);
                });
    }

    default Mono<Void> deleteAllHard() {
        return Mono.error(new ServiceException(
                "00003",
                ExceptionMessageUtil.getMultiLingualMessage("00003"),
                ErrorConstant.CATEGORY.TD,
                ErrorConstant.SEVERITY.I
        ));
    }

    default Flux<T> findAllSafe() {
        return Flux.error(new ServiceException(
                "00004",
                ExceptionMessageUtil.getMultiLingualMessage("00004"),
                ErrorConstant.CATEGORY.TD,
                ErrorConstant.SEVERITY.I
        ));
    }

    default Mono<Page<T>> findAll(Pageable pageable) {
        int pageSize = Math.min(pageable.getPageSize(), DEFAULT_PAGE_MAX_SIZE);
        Pageable limited = PageRequest.of(
                pageable.getPageNumber(),
                pageSize,
                pageable.getSort()
        );

        return this.count()
                .zipWith(this.findAll()
                        .skip((long) limited.getPageNumber() * limited.getPageSize())
                        .take(limited.getPageSize())
                        .collectList()
                )
                .map(tuple -> new PageImpl<>(
                        tuple.getT2(),
                        limited,
                        tuple.getT1()
                ));
    }
}