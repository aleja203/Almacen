
package com.egg.almacen.DTO;

import java.util.Set;


public class IngresoDeMercaderiaDTO {
    
    private String proveedorId;
    private String observaciones;
    private double totalCompra;
    private Set<DetalleIngresoDTO> detalles;

    public IngresoDeMercaderiaDTO() {
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

    public Set<DetalleIngresoDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(Set<DetalleIngresoDTO> detalles) {
        this.detalles = detalles;
    }

    
    @Override
public String toString() {
    return "IngresoDeMercaderiaDTO{" +
            "proveedor=" + proveedorId +
            ", observaciones='" + observaciones + '\'' +
            ", totalCompra=" + totalCompra +
            ", detalles=" + detalles +
            '}';
}
    
}
