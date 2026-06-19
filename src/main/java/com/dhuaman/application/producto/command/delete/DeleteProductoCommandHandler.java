package com.dhuaman.application.producto.command.delete;

import com.dhuaman.application.shared.CommandHandler;
import com.dhuaman.domain.exception.NotFoundException;
import com.dhuaman.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteProductoCommandHandler
        implements CommandHandler<DeleteProductoCommand, Mono<Void>> {

    private final ProductoRepository productoRepository;
    private final DeleteProductoCommandValidator validator;

    public DeleteProductoCommandHandler(ProductoRepository productoRepository,
                                        DeleteProductoCommandValidator validator) {
        this.productoRepository = productoRepository;
        this.validator = validator;
    }

    @Override
    public Mono<Void> handle(DeleteProductoCommand command) {
        return Mono.fromRunnable(() -> validator.validate(command))
                .then(Mono.defer(() -> productoRepository.existePorId(command.id())))
                .flatMap(exists -> exists
                        ? productoRepository.eliminar(command.id())
                        : Mono.error(new NotFoundException("Producto no encontrado con id: " + command.id())));
    }
}
