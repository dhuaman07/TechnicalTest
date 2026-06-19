package com.dhuaman.application.producto.command.create;

import com.dhuaman.application.exception.ValidationException;
import com.dhuaman.domain.exception.ConflictException;
import com.dhuaman.domain.model.Producto;
import com.dhuaman.domain.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductoCommandHandlerTest {

    @Mock
    private ProductoRepository productoRepository;

    private CreateProductoCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateProductoCommandHandler(productoRepository, new CreateProductoCommandValidator());
    }

    @Test
    void debeCrearProductoCuandoNoExiste() {
        var command = new CreateProductoCommand("Laptop", "Descripcion", 1500.0, 10);
        var producto = Producto.crear(1L, "Laptop", "Descripcion", 1500.0, 10);

        when(productoRepository.existePorNombre("Laptop")).thenReturn(Mono.just(false));
        when(productoRepository.guardar(any())).thenReturn(Mono.just(producto));

        StepVerifier.create(handler.handle(command))
                .expectNextMatches(r -> r.id().equals(1L) && r.nombre().equals("Laptop"))
                .verifyComplete();

        verify(productoRepository).guardar(any());
    }

    @Test
    void debeLanzarConflictExceptionCuandoNombreYaExiste() {
        var command = new CreateProductoCommand("Laptop", "Descripcion", 1500.0, 10);

        when(productoRepository.existePorNombre("Laptop")).thenReturn(Mono.just(true));

        StepVerifier.create(handler.handle(command))
                .expectError(ConflictException.class)
                .verify();

        verify(productoRepository, never()).guardar(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoNombreEsBlanco() {
        var command = new CreateProductoCommand("", "Descripcion", 1500.0, 10);

        StepVerifier.create(handler.handle(command))
                .expectError(ValidationException.class)
                .verify();

        verify(productoRepository, never()).existePorNombre(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoPrecioEsInvalido() {
        var command = new CreateProductoCommand("Laptop", "Descripcion", -10.0, 10);

        StepVerifier.create(handler.handle(command))
                .expectError(ValidationException.class)
                .verify();

        verify(productoRepository, never()).existePorNombre(any());
    }
}
