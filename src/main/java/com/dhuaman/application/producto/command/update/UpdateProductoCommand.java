package com.dhuaman.application.producto.command.update;

public record UpdateProductoCommand(Long id, String nombre, String descripcion, Double precio, Integer stock ) {}
