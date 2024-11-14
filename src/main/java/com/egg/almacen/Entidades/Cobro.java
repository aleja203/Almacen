
package com.egg.almacen.Entidades;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Cobro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date fecha;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @OneToMany(mappedBy = "cobro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FormaDePago> formasDePago;
    @OneToMany(mappedBy = "cobro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FormaPagoCobro> formasPago;
    
    private Double totalCobro;

    public Cobro() {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<FormaDePago> getFormasDePago() {
        return formasDePago;
    }

    public void setFormasDePago(Set<FormaDePago> formasDePago) {
        this.formasDePago = formasDePago;
    }

    public Double getTotalCobro() {
        return totalCobro;
    }

    public void setTotalCobro(Double totalCobro) {
        this.totalCobro = totalCobro;
    }

    public Set<FormaPagoCobro> getFormasPago() {
        return formasPago;
    }

    public void setFormasPago(Set<FormaPagoCobro> formasPago) {
        this.formasPago = formasPago;
    }
    
    
    
}
