package com.dhuaman.application.producto.command.update;

import com.dhuaman.application.exception.ValidationException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductoCommandHandlerTest {

    @Mock
    private ProductoRepository productoRepository;

    private UpdateProductoCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateProductoCommandHandler(productoRepository, new UpdateProductoCommandValidator());
    }

    @Test
    void debeActualizarProductoCuandoExiste() {
        var command = new UpdateProductoCommand(1L, "Laptop Pro", "Nueva desc", 2000.0, 5);
        var productoExistente = Producto.crear(1L, "Laptop", "Desc vieja", 1500.0, 10);
        var productoActualizado = Producto.crear(1L, "Laptop Pro", "Nueva desc", 2000.0, 5);

        when(productoRepository.buscarPorId(1L)).thenReturn(Mono.just(productoExistente));
        when(productoRepository.guardar(any())).thenReturn(Mono.just(productoActualizado));

        StepVerifier.create(handler.handle(command))
                .expectNextMatches(r -> r.nombre().equals("Laptop Pro") && r.precio().equals(2000.0))
                .verifyComplete();

        verify(productoRepository).guardar(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoProductoNoExiste() {
        var command = new UpdateProductoCommand(99L, "Laptop Pro", "Desc", 2000.0, 5);

        when(productoRepository.buscarPorId(99L)).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(command))
                .expectError(NotFoundException.class)
                .verify();

        verify(productoRepository, never()).guardar(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoIdEsInvalido() {
        var command = new UpdateProductoCommand(0L, "Laptop Pro", "Desc", 2000.0, 5);

        StepVerifier.create(handler.handle(command))
                .expectError(ValidationException.class)
                .verify();

        verify(productoRepository, never()).buscarPorId(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoNombreEsBlanco() {
        var command = new UpdateProductoCommand(1L, "", "Desc", 2000.0, 5);

        StepVerifier.create(handler.handle(command))
                .expectError(ValidationException.class)
                .verify();

        verify(productoRepository, never()).buscarPorId(any());
    }
}
