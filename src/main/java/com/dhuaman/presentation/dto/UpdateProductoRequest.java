package com.dhuaman.presentation.dto;

public record UpdateProductoRequest(
        String nombre,
        String descripcion,
        Double precio,
        Integer stock
) {}
