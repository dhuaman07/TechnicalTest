package com.dhuaman.application.producto.query.getall;

import com.dhuaman.domain.model.Producto;
import com.dhuaman.domain.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllProductosQueryHandlerTest {

    @Mock
    private ProductoRepository productoRepository;

    private GetAllProductosQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAllProductosQueryHandler(productoRepository);
    }

    @Test
    void debeRetornarTodosLosProductos() {
        var p1 = Producto.crear(1L, "Laptop", "Desc", 1500.0, 10);
        var p2 = Producto.crear(2L, "Mouse", null, 25.0, 50);

        when(productoRepository.listarTodos()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(handler.handle(new GetAllProductosQuery()))
                .expectNextMatches(r -> r.id().equals(1L) && r.nombre().equals("Laptop"))
                .expectNextMatches(r -> r.id().equals(2L) && r.nombre().equals("Mouse"))
                .verifyComplete();
    }

    @Test
    void debeRetornarFluxVacioCuandoNoHayProductos() {
        when(productoRepository.listarTodos()).thenReturn(Flux.empty());

        StepVerifier.create(handler.handle(new GetAllProductosQuery()))
                .verifyComplete();
    }
}
