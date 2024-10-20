
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Movimiento;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Repositorios.MovimientoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MovimientoServicio {
    
    @Autowired
    private MovimientoRepositorio movimientoRepositorio;
    
    
    public List<Movimiento> listarMovimiento() {
    
        List<Movimiento> movimientos = new ArrayList();
        movimientos = movimientoRepositorio.findAll();

        return movimientos;
    }

     
}
