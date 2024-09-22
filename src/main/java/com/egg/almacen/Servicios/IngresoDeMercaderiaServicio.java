
package com.egg.almacen.Servicios;

import com.egg.almacen.DTO.IngresoDeMercaderiaDTO;
import com.egg.almacen.Entidades.DetalleIngreso;
import com.egg.almacen.Entidades.IngresoDeMercaderia;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.IngresoDeMercaderiaRepositorio;
import com.egg.almacen.Repositorios.MovimientoRepositorio;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.ProveedorRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngresoDeMercaderiaServicio {
    
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private IngresoDeMercaderiaRepositorio ingresoDeMercaderiaRepositorio;
    @Autowired
    private MovimientoServicio movimientoServicio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
   
@Transactional
    public Map<String, Object> crearIngresoDeMercaderia(IngresoDeMercaderiaDTO ingresoDeMercaderiaDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            IngresoDeMercaderia ingresoDeMercaderia = new IngresoDeMercaderia();

            // Verificar si el clienteId no es nulo ni vacío
            if (ingresoDeMercaderiaDTO.getProveedorId() != null) {
                // Buscar el cliente por su ID
                Proveedor proveedor = proveedorRepositorio.findById(ingresoDeMercaderiaDTO.getProveedorId())
                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + ingresoDeMercaderiaDTO.getProveedorId()));
                // Asignar el cliente encontrado
                ingresoDeMercaderia.setProveedor(proveedor);
            }

            // Continuar con la asignación de los demás campos de la venta
            ingresoDeMercaderia.setObservaciones(ingresoDeMercaderiaDTO.getObservaciones());
            ingresoDeMercaderia.setTotalCompra(ingresoDeMercaderiaDTO.getTotalCompra());
            ingresoDeMercaderia.setFecha(new Date());  // Establecer la fecha actual

            // Convertir DTOs de detalles a entidades
            Set<DetalleIngreso> detalles = ingresoDeMercaderiaDTO.getDetalles().stream()
                    .map(detalleDTO -> {
                        DetalleIngreso detalle = new DetalleIngreso();
                        detalle.setCantidad(detalleDTO.getCantidad());
                        detalle.setPrecioCompra(detalleDTO.getPrecioCompra());
                        detalle.setTotal(detalleDTO.getTotal());

                        // Buscar el producto por código de barras
                        Producto producto = productoRepositorio.findByCodigo(detalleDTO.getProducto());

                        if (producto != null) {
                            detalle.setProducto(producto);
                        } else {
                            throw new RuntimeException("Producto no encontrado: " + detalleDTO.getProducto());
                        }

                        detalle.setIngresoDeMercaderia(ingresoDeMercaderia);  // Establecer la relación bidireccional
                        return detalle;
                    })
                    .collect(Collectors.toSet());

            ingresoDeMercaderia.setDetalles(detalles);

            // Guardar la entidad Venta en la base de datos
            ingresoDeMercaderiaRepositorio.save(ingresoDeMercaderia);

            // Devolver un mensaje de éxito
            response.put("message", "Ingeso registrado exitosamente.");
            return response;
        } catch (RuntimeException e) {
            // Captura de errores específicos (Producto no encontrado o Cliente no encontrado)
            response.put("error", "Error al registrar el ingreso: " + e.getMessage());
            return response;
        } catch (Exception e) {
            // Captura de otros errores generales
            response.put("error", "Error inesperado al registrar el ingreso.");
            return response;
        }
    }    
}
