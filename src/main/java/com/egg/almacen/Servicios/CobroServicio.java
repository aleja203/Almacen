
package com.egg.almacen.Servicios;

import com.egg.almacen.DTO.CobroDTO;
import com.egg.almacen.DTO.FormaPagoCobroDTO;
import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.Cobro;
import com.egg.almacen.Entidades.CuentaCorriente;
import com.egg.almacen.Entidades.FormaDePago;
import com.egg.almacen.Entidades.FormaPagoCobro;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import com.egg.almacen.Repositorios.CobroRepositorio;
import com.egg.almacen.Repositorios.CuentaCorrienteRepositorio;
import com.egg.almacen.Repositorios.FormaDePagoRepositorio;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CobroServicio {
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private CobroRepositorio cobroRepositorio;
    @Autowired
    private FormaDePagoRepositorio formaDePagoRepositorio;
    @Autowired
    private CuentaCorrienteRepositorio cuentaCorrienteRepositorio;
    
public Map<String, Object> crearCobro(CobroDTO cobroDTO) {
    Map<String, Object> response = new HashMap<>();

    try {
        Cobro cobro = new Cobro();
        final Cliente cliente;

        if (cobroDTO.getClienteId() != null) {
            cliente = clienteRepositorio.findById(cobroDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + cobroDTO.getClienteId()));
            cobro.setCliente(cliente);
        } else {
            cliente = null;
        }

        cobro.setTotalCobro(cobroDTO.getTotalCobro());
        System.out.println("Valor de totalCobro recibido: " + cobroDTO.getTotalCobro());

        cobro.setFecha(new Date());
        
        cobroRepositorio.save(cobro);

        // Crear una sola instancia de CuentaCorriente
        CuentaCorriente cuentaCorriente = new CuentaCorriente();
        cuentaCorriente.setFecha(new Date());
        cuentaCorriente.setTipo("COBRO");
        
        cuentaCorriente.setCliente(cliente != null ? cliente : null);
        cuentaCorriente.setFacturaRecibo(cobro.getId());

        // Calcular el importe total fuera del stream
        double importeTotal = 0.0;
        for (FormaPagoCobroDTO formaPagoDTO : cobroDTO.getFormasPago()) {
            importeTotal += formaPagoDTO.getImporte();
        }

        // Procesar las formas de pago y acumular en el Set
        Set<FormaPagoCobro> formasPago = cobroDTO.getFormasPago().stream()
                .map(formaPagoDTO -> {
                    FormaPagoCobro formaPagoCobro = new FormaPagoCobro();
                    formaPagoCobro.setTipoPago(formaPagoDTO.getTipoPago());

                    if (formaPagoDTO.getFormaDePago() == null) {
                        throw new IllegalArgumentException("FormaDePagoId no puede ser null para tipo de pago " + formaPagoDTO.getTipoPago());
                    }

                    FormaDePago formaDePago = formaDePagoRepositorio.findById(formaPagoDTO.getFormaDePago())
                            .orElseThrow(() -> new EntityNotFoundException("FormaDePago no encontrada con id: " + formaPagoDTO.getFormaDePago()));

                    formaPagoCobro.setFormaDePago(formaDePago);
                    formaPagoCobro.setImporte(formaPagoDTO.getImporte());
                    formaPagoCobro.setCobro(cobro);

                    System.out.println("Valor de tipoPago en formaPagoDTO: " + formaPagoDTO.getTipoPago());

                    return formaPagoCobro;
                })
                .collect(Collectors.toSet());

        // Asignar el importe acumulado a cuentaCorriente
        cuentaCorriente.setImporte(importeTotal);

        // Actualizar saldo del cliente con el importe total y guardar en repositorio
        if (cliente != null) {
            cliente.setSaldo(cliente.getSaldo() + importeTotal);
            clienteRepositorio.save(cliente);
        }

        cobro.setFormasPago(formasPago);

        // Guardar el cobro y la cuenta corriente
        cobroRepositorio.save(cobro);
        cuentaCorrienteRepositorio.save(cuentaCorriente);

        response.put("message", "Cobro registrado exitosamente.");
        return response;
    } catch (RuntimeException e) {
        response.put("error", "Error al registrar el cobro: " + e.getMessage());
        return response;
    } catch (Exception e) {
        response.put("error", "Error inesperado al registrar el cobro.");
        return response;
    }
}

   
    
}
