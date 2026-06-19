package com.dhuaman.application.producto.command.delete;

import com.dhuaman.application.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteProductoCommandValidatorTest {

    private final DeleteProductoCommandValidator validator = new DeleteProductoCommandValidator();

    @Test
    void debeValidarCuandoIdEsCorrecto() {
        var command = new DeleteProductoCommand(1L);
        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoIdEsNulo() {
        var command = new DeleteProductoCommand(null);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoIdEsCero() {
        var command = new DeleteProductoCommand(0L);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }
}
