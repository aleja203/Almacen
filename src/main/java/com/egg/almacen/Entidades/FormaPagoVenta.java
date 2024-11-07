
package com.egg.almacen.Entidades;

import com.egg.almacen.Enumeraciones.TipoFormaPago;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class FormaPagoVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private TipoFormaPago tipoPago; // Tipo de forma de pago (EFECTIVO, TARJETA, etc.)

    @ManyToOne
    @JoinColumn(name = "formaDePago")
    private FormaDePago formaDePago;

    private Double importe; // Importe de la forma de pago

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta; // Relación con la entidad Venta

    public FormaPagoVenta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoFormaPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoFormaPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public FormaDePago getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(FormaDePago formaDePago) {
        this.formaDePago = formaDePago;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    
}
