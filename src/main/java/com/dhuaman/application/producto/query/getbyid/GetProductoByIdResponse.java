package com.dhuaman.application.producto.query.getbyid;

import com.dhuaman.domain.model.Producto;

public record GetProductoByIdResponse(Long id, String nombre, String descripcion, Double precio, Integer stock) {
    public static GetProductoByIdResponse from(Producto p) {
        return new GetProductoByIdResponse(p.getId(), p.getNombre(), p.getDescripcion().orElse(null), p.getPrecio(), p.getStock());
    }
}
