
package com.egg.almacen.Entidades;


import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Producto {

    @Id
    private String codigo;
    private String descripcion;
    @ManyToOne
    private Rubro rubro;
    @ManyToOne
    private SubRubro subRubro;
    private Integer stock;
    private Double costo;
    private Double precioDeVenta;
    @Temporal(TemporalType.DATE)
    private Date alta;

    public Producto() {
        this.stock = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public SubRubro getSubRubro() {
        return subRubro;
    }

    public void setSubRubro(SubRubro subRubro) {
        this.subRubro = subRubro;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Double getPrecioDeVenta() {
        return precioDeVenta;
    }

    public void setPrecioDeVenta(Double precioDeVenta) {
        this.precioDeVenta = precioDeVenta;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
        this.alta = alta;
    }

   
}
