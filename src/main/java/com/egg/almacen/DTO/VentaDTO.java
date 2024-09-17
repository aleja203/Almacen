
package com.egg.almacen.DTO;

import java.util.Set;


public class VentaDTO {
    
    private String cliente;
    private String observaciones;
    private double totalVenta;
    private Set<DetalleVentaDTO> detalles;

    public VentaDTO() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public Set<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(Set<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }

    
}
