package com.dhuaman.infrastructure.persistence.repository;

import com.dhuaman.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface ProductoR2DBCRepository extends ReactiveCrudRepository<ProductoEntity, Long> {
    Mono<Boolean> existsByNombreIgnoreCase(String nombre);
}
