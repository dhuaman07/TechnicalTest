package com.dhuaman.application.producto.query.getall;

import com.dhuaman.application.shared.QueryHandler;
import com.dhuaman.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetAllProductosQueryHandler
        implements QueryHandler<GetAllProductosQuery, Flux<GetAllProductosResponse>> {

    private final ProductoRepository productoRepository;

    public GetAllProductosQueryHandler(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Flux<GetAllProductosResponse> handle(GetAllProductosQuery query) {
        return productoRepository.listarTodos()
                .map(GetAllProductosResponse::from);
    }
}
