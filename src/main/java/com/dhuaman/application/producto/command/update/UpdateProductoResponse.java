package com.dhuaman.application.producto.command.update;

import com.dhuaman.domain.model.Producto;

public record UpdateProductoResponse(Long id, String nombre, String descripcion, Double precio, Integer stock) {
    public static UpdateProductoResponse from(Producto p) {
        return new UpdateProductoResponse(p.getId(), p.getNombre(), p.getDescripcion().orElse(null), p.getPrecio(), p.getStock());
    }
}
