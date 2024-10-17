
package com.egg.almacen.DTO;

import java.util.Set;


public class CompraDTO {
    
    private String proveedorId;
    private String observaciones;
    private double totalCompra;
    private Set<DetalleCompraDTO> detalles;

    public CompraDTO() {
    }

    public String getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(String proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public Set<DetalleCompraDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(Set<DetalleCompraDTO> detalles) {
        this.detalles = detalles;
    }

    
    @Override
public String toString() {
    return "CompraDTO{" +
            "proveedor=" + proveedorId +
            ", observaciones='" + observaciones + '\'' +
            ", totalCompra=" + totalCompra +
            ", detalles=" + detalles +
            '}';
}
    
}
