package com.dhuaman.application.producto.command.create;

import com.dhuaman.application.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateProductoCommandValidatorTest {

    private final CreateProductoCommandValidator validator = new CreateProductoCommandValidator();

    @Test
    void debeValidarCuandoLosDatosSonCorrectos() {
        var command = new CreateProductoCommand("Laptop", "Descripcion", 1500.0, 10);
        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoNombreEsNulo() {
        var command = new CreateProductoCommand(null, "Descripcion", 1500.0, 10);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoNombreEsBlanco() {
        var command = new CreateProductoCommand("  ", "Descripcion", 1500.0, 10);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoPrecioEsCero() {
        var command = new CreateProductoCommand("Laptop", "Descripcion", 0.0, 10);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }

    @Test
    void debeLanzarExcepcionCuandoStockEsNegativo() {
        var command = new CreateProductoCommand("Laptop", "Descripcion", 1500.0, -1);
        assertThrows(ValidationException.class, () -> validator.validate(command));
    }
}
