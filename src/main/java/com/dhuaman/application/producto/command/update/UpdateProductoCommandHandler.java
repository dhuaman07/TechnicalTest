package com.dhuaman.application.producto.command.update;

import com.dhuaman.application.shared.CommandHandler;
import com.dhuaman.domain.exception.NotFoundException;
import com.dhuaman.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductoCommandHandler
        implements CommandHandler<UpdateProductoCommand, Mono<UpdateProductoResponse>> {

    private final ProductoRepository productoRepository;
    private final UpdateProductoCommandValidator validator;

    public UpdateProductoCommandHandler(ProductoRepository productoRepository,
                                        UpdateProductoCommandValidator validator) {
        this.productoRepository = productoRepository;
        this.validator = validator;
    }

    @Override
    public Mono<UpdateProductoResponse> handle(UpdateProductoCommand command) {
        return Mono.fromRunnable(() -> validator.validate(command))
                .then(Mono.defer(() -> productoRepository.buscarPorId(command.id())))
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con id: " + command.id())))
                .flatMap(producto -> {
                    producto.actualizarDatos(command.nombre(), command.descripcion(), command.precio(), command.stock());
                    return productoRepository.guardar(producto);
                })
                .map(UpdateProductoResponse::from);
    }
}
