
package com.egg.almacen.Entidades;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date fecha; 
    
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
    
    private String observaciones;
    
//    @ManyToOne
//    private Producto producto;
    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetalleCompra> detalles;
    
    private Double totalCompra;      
    
    public Compra() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

//    public Producto getProducto() {
//        return producto;
//    }
//
//    public void setProducto(Producto producto) {
//        this.producto = producto;
//    }

    public Set<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(Set<DetalleCompra> detalles) {
        this.detalles = detalles;
    }

    public Double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(Double totalCompra) {
        this.totalCompra = totalCompra;
    }

    
    
}
