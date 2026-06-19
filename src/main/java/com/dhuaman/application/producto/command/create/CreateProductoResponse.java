package com.dhuaman.application.producto.command.create;

import com.dhuaman.domain.model.Producto;

public record CreateProductoResponse(Long id, String nombre, String descripcion, Double precio, Integer stock) {
    public static CreateProductoResponse from(Producto p) {
        return new CreateProductoResponse(p.getId(), p.getNombre(), p.getDescripcion().orElse(null), p.getPrecio(), p.getStock());
    }
}
