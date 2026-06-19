package com.dhuaman.application.producto.command.create;

public record CreateProductoCommand(String nombre, String descripcion, Double precio, Integer stock) {}
