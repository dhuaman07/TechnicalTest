package com.dhuaman.presentation.controller;

import com.dhuaman.application.producto.command.create.CreateProductoCommand;
import com.dhuaman.application.producto.command.create.CreateProductoResponse;
import com.dhuaman.application.producto.command.delete.DeleteProductoCommand;
import com.dhuaman.application.producto.command.update.UpdateProductoCommand;
import com.dhuaman.application.producto.command.update.UpdateProductoResponse;
import com.dhuaman.application.producto.query.getall.GetAllProductosQuery;
import com.dhuaman.application.producto.query.getall.GetAllProductosResponse;
import com.dhuaman.application.producto.query.getbyid.GetProductoByIdQuery;
import com.dhuaman.application.producto.query.getbyid.GetProductoByIdResponse;
import com.dhuaman.application.shared.CommandBus;
import com.dhuaman.application.shared.QueryBus;
import com.dhuaman.presentation.dto.CreateProductoRequest;
import com.dhuaman.presentation.dto.UpdateProductoRequest;
import com.dhuaman.presentation.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public ProductoController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<GetAllProductosResponse>>>> listarTodos() {
        Flux<GetAllProductosResponse> result = queryBus.dispatch(new GetAllProductosQuery());
        return result.collectList()
                .map(items -> ResponseEntity.ok(
                        ApiResponse.success("Productos obtenidos exitosamente", items)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<GetProductoByIdResponse>>> buscarPorId(@PathVariable Long id) {
        Mono<GetProductoByIdResponse> result = queryBus.dispatch(new GetProductoByIdQuery(id));
        return result.map(item -> ResponseEntity.ok(
                ApiResponse.success("Producto obtenido exitosamente", item)));
    }

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<CreateProductoResponse>>> crear(
            @RequestBody CreateProductoRequest request) {
        Mono<CreateProductoResponse> result = commandBus.dispatch(
                new CreateProductoCommand(request.nombre(), request.descripcion(), request.precio(), request.stock()));
        return result.map(item -> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado exitosamente", item)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<UpdateProductoResponse>>> actualizar(
            @PathVariable Long id,
            @RequestBody UpdateProductoRequest request) {
        Mono<UpdateProductoResponse> result = commandBus.dispatch(
                new UpdateProductoCommand(id, request.nombre(), request.descripcion(), request.precio(), request.stock()));
        return result.map(item -> ResponseEntity.ok(
                ApiResponse.success("Producto actualizado exitosamente", item)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Void>>> eliminar(@PathVariable Long id) {
        Mono<Void> result = commandBus.dispatch(new DeleteProductoCommand(id));
        return result.thenReturn(ResponseEntity.ok(
                ApiResponse.<Void>success("Producto eliminado exitosamente", null)));
    }
}
