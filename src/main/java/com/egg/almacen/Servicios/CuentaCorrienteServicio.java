
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.CuentaCorriente;
import com.egg.almacen.Repositorios.CuentaCorrienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaCorrienteServicio {
    
    @Autowired
    private CuentaCorrienteRepositorio cuentaCorrienteRepositorio;
    
        public List<CuentaCorriente> listarCuentaCorriente() {
    
        List<CuentaCorriente> cuentasCorrientes = new ArrayList();
        cuentasCorrientes = cuentaCorrienteRepositorio.findAll();

        return cuentasCorrientes;
    }
        
        public List<CuentaCorriente> listarCuentasCorrientesPorDni(Long dni) {
        return cuentaCorrienteRepositorio.findByClienteDni(dni);
    }
    
}
