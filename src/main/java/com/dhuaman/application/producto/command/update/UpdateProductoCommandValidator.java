package com.dhuaman.application.producto.command.update;

import com.dhuaman.application.exception.ValidationException;
import com.dhuaman.application.shared.CommandValidator;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductoCommandValidator implements CommandValidator<UpdateProductoCommand> {

    @Override
    public void validate(UpdateProductoCommand command) {
        if (command.id() == null || command.id() <= 0)
            throw new ValidationException("El id del producto es obligatorio y debe ser mayor a cero");
        if (command.nombre() == null || command.nombre().isBlank())
            throw new ValidationException("El nombre del producto es obligatorio");
        if (command.precio() == null || command.precio() <= 0)
            throw new ValidationException("El precio debe ser mayor a cero");
        if (command.stock() == null || command.stock() <= 0)
            throw new ValidationException("El stock debe ser mayor a cero");
    }
}
