package com.dhuaman.infrastructure.persistence.entity;

import com.dhuaman.domain.model.Producto;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("productos")
public class ProductoEntity {

    @Id
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;

    public ProductoEntity() {}

    public static ProductoEntity from(Producto producto) {
        ProductoEntity entity = new ProductoEntity();
        entity.id = producto.getId();
        entity.nombre = producto.getNombre();
        entity.descripcion = producto.getDescripcion().orElse(null);
        entity.precio = producto.getPrecio();
        entity.stock = producto.getStock();
        return entity;
    }

    public Producto toDomain() {
        return Producto.crear(id, nombre, descripcion, precio, stock);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
