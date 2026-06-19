package com.dhuaman.application.producto.command.create;

import com.dhuaman.application.shared.CommandHandler;
import com.dhuaman.domain.exception.ConflictException;
import com.dhuaman.domain.model.Producto;
import com.dhuaman.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateProductoCommandHandler
        implements CommandHandler<CreateProductoCommand, Mono<CreateProductoResponse>> {

    private final ProductoRepository productoRepository;
    private final CreateProductoCommandValidator validator;

    public CreateProductoCommandHandler(ProductoRepository productoRepository,
                                        CreateProductoCommandValidator validator) {
        this.productoRepository = productoRepository;
        this.validator = validator;
    }

    @Override
    public Mono<CreateProductoResponse> handle(CreateProductoCommand command) {
        return validar(command)
                .then(Mono.fromCallable(() ->
                        Producto.crear(null, command.nombre(), command.descripcion(), command.precio(), command.stock())))
                .flatMap(productoRepository::guardar)
                .map(CreateProductoResponse::from);
    }

    private Mono<Void> validar(CreateProductoCommand command) {
        return Mono.fromRunnable(() -> validator.validate(command))
                .then(Mono.defer(() -> checkNombreUnico(command.nombre())));
    }

    private Mono<Void> checkNombreUnico(String nombre) {
        return productoRepository.existePorNombre(nombre)
                .flatMap(exists -> exists
                        ? Mono.error(new ConflictException("Ya existe un producto con el nombre: " + nombre))
                        : Mono.empty());
    }
}
