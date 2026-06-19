package com.dhuaman.application.producto.query.getall;

import com.dhuaman.domain.model.Producto;

public record GetAllProductosResponse(Long id, String nombre, String descripcion, Double precio, Integer stock) {
    public static GetAllProductosResponse from(Producto p) {
        return new GetAllProductosResponse(p.getId(), p.getNombre(), p.getDescripcion().orElse(null), p.getPrecio(), p.getStock());
    }
}
