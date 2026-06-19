package com.dhuaman.application.producto.query.getbyid;

import com.dhuaman.domain.exception.NotFoundException;
import com.dhuaman.domain.model.Producto;
import com.dhuaman.domain.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductoByIdQueryHandlerTest {

    @Mock
    private ProductoRepository productoRepository;

    private GetProductoByIdQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetProductoByIdQueryHandler(productoRepository);
    }

    @Test
    void debeRetornarProductoCuandoExiste() {
        var producto = Producto.crear(1L, "Laptop", "Descripcion", 1500.0, 10);

        when(productoRepository.buscarPorId(1L)).thenReturn(Mono.just(producto));

        StepVerifier.create(handler.handle(new GetProductoByIdQuery(1L)))
                .expectNextMatches(r -> r.id().equals(1L) && r.nombre().equals("Laptop"))
                .verifyComplete();
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoNoExiste() {
        when(productoRepository.buscarPorId(99L)).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(new GetProductoByIdQuery(99L)))
                .expectError(NotFoundException.class)
                .verify();
    }
}
