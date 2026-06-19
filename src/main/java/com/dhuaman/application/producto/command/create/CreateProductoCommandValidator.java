package com.dhuaman.application.producto.command.create;

import com.dhuaman.application.exception.ValidationException;
import com.dhuaman.application.shared.CommandValidator;
import org.springframework.stereotype.Component;

@Component
public class CreateProductoCommandValidator implements CommandValidator<CreateProductoCommand> {

    @Override
    public void validate(CreateProductoCommand command) {
        if (command.nombre() == null || command.nombre().isBlank())
            throw new ValidationException("El nombre del producto es obligatorio");
        if (command.precio() == null || command.precio() <= 0)
            throw new ValidationException("El precio debe ser mayor a cero");
        if (command.stock() == null || command.stock() < 0)
            throw new ValidationException("El stock no puede ser negativo");
    }
}
