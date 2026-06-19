package com.dhuaman.application.producto.query.getbyid;

import com.dhuaman.application.shared.QueryHandler;
import com.dhuaman.domain.exception.NotFoundException;
import com.dhuaman.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProductoByIdQueryHandler
        implements QueryHandler<GetProductoByIdQuery, Mono<GetProductoByIdResponse>> {

    private final ProductoRepository productoRepository;

    public GetProductoByIdQueryHandler(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Mono<GetProductoByIdResponse> handle(GetProductoByIdQuery query) {
        return productoRepository.buscarPorId(query.id())
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con id: " + query.id())))
                .map(GetProductoByIdResponse::from);
    }
}
