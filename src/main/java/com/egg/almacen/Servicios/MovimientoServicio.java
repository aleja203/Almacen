
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Movimiento;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Repositorios.MovimientoRepositorio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MovimientoServicio {
    
    @Autowired
    private MovimientoRepositorio movimientoRepositorio;
    
    
    
    
    public List<Movimiento> listarMovimiento() {

        List<Movimiento> movimientos = movimientoRepositorio.findAllOrderedByIdDesc();
        

        if (movimientos.isEmpty()) {
            System.out.println("No se encontraron movimientos para la factura con ID: " + Long.MAX_VALUE);
        } else {
            // Imprimir cada movimiento con sus atributos clave
            System.out.println("Movimientos ordenados por fecha y factura:");
            movimientos.forEach(movimiento -> {
                System.out.println("Fecha: " + movimiento.getFecha()
                        + ", Movimiento: " + movimiento.getId()
                        + ", Tipo: " + movimiento.getTipo()
                        + ", Producto: " + movimiento.getProducto()
                        + ", Cantidad: " + movimiento.getCantidad());
            });
        }

        return movimientos;
    }
    
    public List<Movimiento> calcularExistencias(List<Movimiento> movimientos) {
    // Variable para rastrear el stock acumulado hacia atrás
    Map<String, Double> existenciasPorProducto = new HashMap<>();

    // Iterar en orden inverso (ya que los movimientos están descendentes)
    for (int i = movimientos.size() - 1; i >= 0; i--) {
        Movimiento movimiento = movimientos.get(i);
        String producto = movimiento.getProducto();
        Double cantidad = movimiento.getCantidad();
        String tipo = movimiento.getTipo();

        // Obtener la existencia actual o inicializarla
        Double stockActual = existenciasPorProducto.getOrDefault(producto, 0.0);

        // Actualizar el stock según el tipo de movimiento
        if ("VENTA".equalsIgnoreCase(tipo)) {
            stockActual += cantidad; // Restar en caso de venta
        } else if ("COMPRA".equalsIgnoreCase(tipo)) {
            stockActual += cantidad; // Sumar en caso de compra
        }

        // Guardar el stock actualizado en el movimiento y en el mapa
        movimiento.setExistencia(stockActual);
        existenciasPorProducto.put(producto, stockActual);
    }

    return movimientos;
}


     
}
