
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
    public void crearFormaDePago(String descripcion, TipoFormaPago idTipoPago) throws MiException {

        // validar(nombre);
        FormaDePago formaDePago = new FormaDePago();

        formaDePago.setDescripcion(descripcion);

        formaDePagoRepositorio.save(formaDePago);

    }

    public List<FormaDePago> listarFormasDePago() {

        List<FormaDePago> formasDePago = new ArrayList();

        formasDePago = formaDePagoRepositorio.findAll();

        return formasDePago;
    }
    
}
