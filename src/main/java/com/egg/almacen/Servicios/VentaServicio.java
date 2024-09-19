
package com.egg.almacen.Servicios;


import com.egg.almacen.DTO.DetalleVentaDTO;
import com.egg.almacen.DTO.VentaDTO;
import com.egg.almacen.Entidades.DetalleVenta;
import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.DetalleVentaRepositorio;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.VentaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class VentaServicio {
    
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private VentaRepositorio ventaRepositorio;
    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;
    @Autowired
    private MovimientoServicio movimientoServicio;
    
    


    @Transactional
    public Map<String, Object> crearVenta(VentaDTO ventaDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Crear la entidad Venta
            Venta venta = new Venta();
            venta.setCliente(ventaDTO.getCliente());
            venta.setObservaciones(ventaDTO.getObservaciones());
            venta.setTotalVenta(ventaDTO.getTotalVenta());
            venta.setFecha(new Date());  // Establecer la fecha actual

            // Convertir DTOs de detalles a entidades
            Set<DetalleVenta> detalles = ventaDTO.getDetalles().stream()
                    .map(detalleDTO -> {
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setCantidad(detalleDTO.getCantidad());
                        detalle.setPrecioVenta(detalleDTO.getPrecioVenta());
                        detalle.setTotal(detalleDTO.getTotal());

                        // Buscar el producto por código de barras
                        Producto producto = productoRepositorio.findByCodigo(detalleDTO.getProducto());

                        if (producto != null) {
                            detalle.setProducto(producto);
                        } else {
                            throw new RuntimeException("Producto no encontrado: " + detalleDTO.getProducto());
                        }

                        detalle.setVenta(venta);  // Establecer la relación bidireccional
                        return detalle;
                    })
                    .collect(Collectors.toSet());

            venta.setDetalles(detalles);

            // Guardar la entidad Venta en la base de datos
            ventaRepositorio.save(venta);

            // Devolvemos un mensaje de éxito
            response.put("message", "Venta registrada exitosamente.");
            return response;

        } catch (RuntimeException e) {
            // Captura de errores específicos (Producto no encontrado)
            response.put("error", "Error al registrar la venta: " + e.getMessage());
            return response;

        } catch (Exception e) {
            // Captura de otros errores generales
            response.put("error", "Error inesperado al registrar la venta.");
            return response;
        }
    }

    
}
