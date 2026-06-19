package com.dhuaman.application.producto.command.update;

import com.dhuaman.application.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProductoCommandValidatorTest {

    private final UpdateProductoCommandValidator validator = new UpdateProductoCommandValidator();

    @Test
    void debeValidarCuandoLosDatosSonCorrectos() {
        var command = new UpdateProductoCommand(1L, "Laptop Pro", "Descripcion", 2000.0, 5);
        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoIdEsNulo() {
        var command = new UpdateProductoCommand(null, "Laptop Pro", "Descripcion", 2000.0, 5);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoNombreEsBlanco() {
        var command = new UpdateProductoCommand(1L, "", "Descripcion", 2000.0, 5);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoPrecioEsNegativo() {
        var command = new UpdateProductoCommand(1L, "Laptop Pro", "Descripcion", -1.0, 5);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoStockEsCero() {
        var command = new UpdateProductoCommand(1L, "Laptop Pro", "Descripcion", 2000.0, 0);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }
}
