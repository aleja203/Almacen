
package com.egg.almacen.DTO;

import java.util.Set;


public class VentaDTO {
    
    private Long clienteId;
    private String observaciones;
    private double totalVenta;
    private Set<DetalleVentaDTO> detalles;

    public VentaDTO() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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

    @Override
public String toString() {
    return "VentaDTO{" +
            "cliente=" + clienteId +
            ", observaciones='" + observaciones + '\'' +
            ", totalVenta=" + totalVenta +
            ", detalles=" + detalles +
            '}';
}


    
}
