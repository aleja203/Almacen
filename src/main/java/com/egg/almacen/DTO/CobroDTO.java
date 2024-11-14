
package com.egg.almacen.DTO;

import java.util.Set;


public class CobroDTO {
    
    private Long clienteId;
    private double totalCobro;
    private Set<FormaPagoCobroDTO> formasPago;

    public CobroDTO() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public double getTotalCobro() {
        return totalCobro;
    }

    public void setTotalCobro(double totalCobro) {
        this.totalCobro = totalCobro;
    }

    public Set<FormaPagoCobroDTO> getFormasPago() {
        return formasPago;
    }

    public void setFormasPago(Set<FormaPagoCobroDTO> formasPago) {
        this.formasPago = formasPago;
    }
    
    @Override
    public String toString() {
    return "CobroDTO{" +
            "cliente=" + clienteId +
            ", importe=" + totalCobro +
            ", formasPago=" + formasPago +
            '}';
    }
}
