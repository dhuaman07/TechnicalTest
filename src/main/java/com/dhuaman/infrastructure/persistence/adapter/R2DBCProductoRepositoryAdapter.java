package com.dhuaman.infrastructure.persistence.adapter;

import com.dhuaman.domain.exception.DomainException;
import com.dhuaman.domain.model.Producto;
import com.dhuaman.domain.repository.ProductoRepository;
import com.dhuaman.infrastructure.persistence.entity.ProductoEntity;
import com.dhuaman.infrastructure.persistence.repository.ProductoR2DBCRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class R2DBCProductoRepositoryAdapter implements ProductoRepository {

    private final ProductoR2DBCRepository r2dbcRepository;

    public R2DBCProductoRepositoryAdapter(ProductoR2DBCRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<Producto> guardar(Producto producto) {
        return r2dbcRepository.save(ProductoEntity.from(producto))
                .map(ProductoEntity::toDomain)
                .onErrorResume(
                        ex -> !(ex instanceof DomainException),
                        ex -> Mono.error(new DomainException("Error al persistir el producto: " + ex.getMessage())));
    }

    @Override
    public Mono<Producto> buscarPorId(Long id) {
        return r2dbcRepository.findById(id)
                .map(ProductoEntity::toDomain);
    }

    @Override
    public Flux<Producto> listarTodos() {
        return r2dbcRepository.findAll()
                .map(ProductoEntity::toDomain);
    }

    @Override
    public Mono<Void> eliminar(Long id) {
        return r2dbcRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existePorId(Long id) {
        return r2dbcRepository.existsById(id);
    }

    @Override
    public Mono<Boolean> existePorNombre(String nombre) {
        return r2dbcRepository.existsByNombreIgnoreCase(nombre);
    }
}
