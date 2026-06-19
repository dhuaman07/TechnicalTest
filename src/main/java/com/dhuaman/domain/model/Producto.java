package com.dhuaman.domain.model;

import com.dhuaman.domain.exception.DomainException;

import java.util.Optional;

public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;

    private Producto() {}

    public static Producto crear(Long id, String nombre, String descripcion, Double precio, Integer stock) {
        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);

        Producto producto = new Producto();
        producto.id = id;
        producto.nombre = nombre;
        producto.descripcion = descripcion;
        producto.precio = precio;
        producto.stock = stock;
        return producto;
    }

    public void actualizarDatos(String nombre, String descripcion, Double precio, Integer stock) {
        validarNombre(nombre);
        validarPrecio(precio);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public void reducirStock(Integer cantidad) {
        if (cantidad <= 0) throw new DomainException("La cantidad debe ser mayor a cero");
        if (cantidad > this.stock) throw new DomainException("Stock insuficiente");
        this.stock -= cantidad;
    }

    public void incrementarStock(Integer cantidad) {
        if (cantidad <= 0) throw new DomainException("La cantidad debe ser mayor a cero");
        this.stock += cantidad;
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new DomainException("El nombre del producto es obligatorio");
    }

    private static void validarPrecio(Double precio) {
        if (precio == null || precio <= 0) throw new DomainException("El precio debe ser mayor a cero");
    }

    private static void validarStock(Integer stock) {
        if (stock == null || stock < 0) throw new DomainException("El stock no puede ser negativo");
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Optional<String> getDescripcion() { return Optional.ofNullable(descripcion); }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', precio=" + precio + ", stock=" + stock + "}";
    }
}
