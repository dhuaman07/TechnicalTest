package com.dhuaman.presentation.dto;

public record CreateProductoRequest(
        String nombre,
        String descripcion,
        Double precio,
        Integer stock
) {}
