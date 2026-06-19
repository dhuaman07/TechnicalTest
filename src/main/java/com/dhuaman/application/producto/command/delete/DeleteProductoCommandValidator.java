package com.dhuaman.application.producto.command.delete;

import com.dhuaman.application.exception.ValidationException;
import com.dhuaman.application.shared.CommandValidator;
import org.springframework.stereotype.Component;

@Component
public class DeleteProductoCommandValidator implements CommandValidator<DeleteProductoCommand> {

    @Override
    public void validate(DeleteProductoCommand command) {
        if (command.id() == null || command.id() <= 0)
            throw new ValidationException("El id del producto es obligatorio y debe ser mayor a cero");
    }
}
