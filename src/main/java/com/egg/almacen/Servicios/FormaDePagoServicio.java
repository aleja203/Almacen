
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.FormaDePago;
import com.egg.almacen.Enumeraciones.TipoFormaPago;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.FormaDePagoRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormaDePagoServicio {
    
    @Autowired
    FormaDePagoRepositorio formaDePagoRepositorio;

    @Transactional
    public void crearFormaDePago(String descripcion, TipoFormaPago tipoFormaPago) throws MiException {
        // Valida la descripción y el tipo de forma de pago
        validar(descripcion, tipoFormaPago);

        // Crea una nueva instancia de FormaDePago
        FormaDePago formaDePago = new FormaDePago();
        formaDePago.setDescripcion(descripcion);
        formaDePago.setTipo(tipoFormaPago); // Asigna el tipo de forma de pago

        // Guarda la forma de pago en la base de datos
        formaDePagoRepositorio.save(formaDePago);
    }

    private void validar(String descripcion, TipoFormaPago tipoFormaPago) throws MiException {
        // Asegúrate de validar la descripción y que el tipo de forma de pago no sea nulo
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException("La descripción no puede estar vacía.");
        }
        if (tipoFormaPago == null) {
            throw new MiException("El tipo de forma de pago no puede ser nulo.");
        }
    }

    public List<FormaDePago> listarFormasDePago() {

        List<FormaDePago> formasDePago = new ArrayList();

        formasDePago = formaDePagoRepositorio.findAll();

        return formasDePago;
    }
    
    private void validar(String descripcion, String idTipoFormaPago) throws MiException {

        // Validar la descripción
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException("La descripción no puede ser nula o estar vacía");
        }

        // Validar el tipo de forma de pago contra el enum TipoFormaPago
        try {
            TipoFormaPago tipoFormaPago = TipoFormaPago.valueOf(idTipoFormaPago);
        } catch (IllegalArgumentException e) {
            throw new MiException("El tipo de forma de pago seleccionado no es válido.");
        }
    }
}
