package com.dhuaman.application.producto.command.delete;

import com.dhuaman.application.exception.ValidationException;
import com.dhuaman.domain.exception.NotFoundException;
import com.dhuaman.domain.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductoCommandHandlerTest {

    @Mock
    private ProductoRepository productoRepository;

    private DeleteProductoCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DeleteProductoCommandHandler(productoRepository, new DeleteProductoCommandValidator());
    }

    @Test
    void debeEliminarProductoCuandoExiste() {
        var command = new DeleteProductoCommand(1L);

        when(productoRepository.existePorId(1L)).thenReturn(Mono.just(true));
        when(productoRepository.eliminar(1L)).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(command))
                .verifyComplete();

        verify(productoRepository).eliminar(1L);
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoProductoNoExiste() {
        var command = new DeleteProductoCommand(99L);

        when(productoRepository.existePorId(99L)).thenReturn(Mono.just(false));

        StepVerifier.create(handler.handle(command))
                .expectError(NotFoundException.class)
                .verify();

        verify(productoRepository, never()).eliminar(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoIdEsInvalido() {
        var command = new DeleteProductoCommand(0L);

        StepVerifier.create(handler.handle(command))
                .expectError(ValidationException.class)
                .verify();

        verify(productoRepository, never()).existePorId(any());
    }
}
