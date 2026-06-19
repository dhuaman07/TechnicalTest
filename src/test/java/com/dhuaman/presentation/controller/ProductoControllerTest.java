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
import com.dhuaman.domain.exception.NotFoundException;
import com.dhuaman.presentation.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@WebFluxTest(controllers = ProductoController.class)
@Import(GlobalExceptionHandler.class)
class ProductoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommandBus commandBus;

    @MockBean
    private QueryBus queryBus;

    @Test
    void debeListarTodosLosProductos() {
        var response = new GetAllProductosResponse(1L, "Laptop", "Desc", 1500.0, 10);

        doReturn(Flux.just(response)).when(queryBus).dispatch(any(GetAllProductosQuery.class));

        webTestClient.get()
                .uri("/api/v1/productos")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.isSuccess").isEqualTo(true)
                .jsonPath("$.data[0].nombre").isEqualTo("Laptop");
    }

    @Test
    void debeBuscarProductoPorId() {
        var response = new GetProductoByIdResponse(1L, "Laptop", "Desc", 1500.0, 10);

        doReturn(Mono.just(response)).when(queryBus).dispatch(any(GetProductoByIdQuery.class));

        webTestClient.get()
                .uri("/api/v1/productos/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.isSuccess").isEqualTo(true)
                .jsonPath("$.data.nombre").isEqualTo("Laptop");
    }

    @Test
    void debeRetornar404CuandoProductoNoExiste() {
        doReturn(Mono.error(new NotFoundException("Producto no encontrado con id: 99")))
                .when(queryBus).dispatch(any(GetProductoByIdQuery.class));

        webTestClient.get()
                .uri("/api/v1/productos/99")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.isSuccess").isEqualTo(false);
    }

    @Test
    void debeCrearProducto() {
        var response = new CreateProductoResponse(1L, "Laptop", "Desc", 1500.0, 10);

        doReturn(Mono.just(response)).when(commandBus).dispatch(any(CreateProductoCommand.class));

        webTestClient.post()
                .uri("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"nombre":"Laptop","descripcion":"Desc","precio":1500.0,"stock":10}
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.isSuccess").isEqualTo(true)
                .jsonPath("$.data.id").isEqualTo(1);
    }

    @Test
    void debeActualizarProducto() {
        var response = new UpdateProductoResponse(1L, "Laptop Pro", "Nueva desc", 2000.0, 5);

        doReturn(Mono.just(response)).when(commandBus).dispatch(any(UpdateProductoCommand.class));

        webTestClient.put()
                .uri("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"nombre":"Laptop Pro","descripcion":"Nueva desc","precio":2000.0,"stock":5}
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.isSuccess").isEqualTo(true)
                .jsonPath("$.data.nombre").isEqualTo("Laptop Pro");
    }

    @Test
    void debeEliminarProducto() {
        doReturn(Mono.empty()).when(commandBus).dispatch(any(DeleteProductoCommand.class));

        webTestClient.delete()
                .uri("/api/v1/productos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.isSuccess").isEqualTo(true);
    }
}
