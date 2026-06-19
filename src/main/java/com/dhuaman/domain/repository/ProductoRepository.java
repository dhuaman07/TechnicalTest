package com.dhuaman.domain.repository;

import com.dhuaman.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepository {
    Mono<Producto> guardar(Producto producto);
    Mono<Producto> buscarPorId(Long id);
    Flux<Producto> listarTodos();
    Mono<Void> eliminar(Long id);
    Mono<Boolean> existePorId(Long id);
    Mono<Boolean> existePorNombre(String nombre);
}