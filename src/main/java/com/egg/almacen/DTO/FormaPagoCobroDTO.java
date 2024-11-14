
package com.egg.almacen.DTO;

import com.egg.almacen.Enumeraciones.TipoFormaPago;


public class FormaPagoCobroDTO {
    
    private TipoFormaPago tipoPago; // Enumeración para tipo de forma de pago
    private Long formaDePago; // Forma de pago específica
    private Double importe; // Importe de la forma de pago

    public FormaPagoCobroDTO() {
    }

    public TipoFormaPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoFormaPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Long getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(Long formaDePago) {
        this.formaDePago = formaDePago;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
    
    
}
