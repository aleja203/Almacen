
package com.egg.almacen.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DetalleIngreso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingresoDeMercaderia_id")
    private IngresoDeMercaderia ingresoDeMercaderia;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Double cantidad;
    private Double precioCompra;
    private Double total;

    public DetalleIngreso() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngresoDeMercaderia getIngresoDeMercaderia() {
        return ingresoDeMercaderia;
    }

    public void setIngresoDeMercaderia(IngresoDeMercaderia ingresoDeMercaderia) {
        this.ingresoDeMercaderia = ingresoDeMercaderia;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }


    
    
}
