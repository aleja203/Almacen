
package com.egg.almacen.Servicios;

import com.egg.almacen.DTO.CompraDTO;
import com.egg.almacen.Entidades.DetalleCompra;
import com.egg.almacen.Entidades.Compra;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.CompraRepositorio;
import com.egg.almacen.Repositorios.MovimientoRepositorio;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.ProveedorRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CompraServicio {
    
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private CompraRepositorio compraRepositorio;
    @Autowired
    private MovimientoServicio movimientoServicio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
   
    @Transactional
    public Map<String, Object> crearCompra(CompraDTO compraDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            Compra compra = new Compra();

            // Verificar si el clienteId no es nulo ni vacío
            // if (compraDTO.getProveedorId() != null) {
            if (compraDTO.getProveedorId() != null && !compraDTO.getProveedorId().trim().isEmpty()) {
                // Buscar el cliente por su ID
                Proveedor proveedor = proveedorRepositorio.findById(compraDTO.getProveedorId())
                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + compraDTO.getProveedorId()));
                // Asignar el cliente encontrado
                compra.setProveedor(proveedor);
            }

            // Continuar con la asignación de los demás campos de la venta
            compra.setObservaciones(compraDTO.getObservaciones());
            compra.setTotalCompra(compraDTO.getTotalCompra());
            compra.setFecha(new Date());  // Establecer la fecha actual

            // Convertir DTOs de detalles a entidades
            Set<DetalleCompra> detalles = compraDTO.getDetalles().stream()
                    .map(detalleDTO -> {
                        DetalleCompra detalle = new DetalleCompra();
                        detalle.setCantidad(detalleDTO.getCantidad());
                        detalle.setCosto(detalleDTO.getCosto());
                        detalle.setTotal(detalleDTO.getTotal());

                        // Buscar el producto por código de barras
                        Producto producto = productoRepositorio.findByCodigo(detalleDTO.getProducto());

                        if (producto != null) {
                            
                             producto.setExistencia(producto.getExistencia() + detalleDTO.getCantidad());

                        // Guardar el producto con la nueva existencia
                        productoRepositorio.save(producto);
                            
                            detalle.setProducto(producto);
                        } else {
                            throw new RuntimeException("Producto no encontrado: " + detalleDTO.getProducto());
                        }

                        detalle.setCompra(compra);  // Establecer la relación bidireccional
                        return detalle;
                    })
                    .collect(Collectors.toSet());

            compra.setDetalles(detalles);

            // Guardar la entidad Venta en la base de datos
            compraRepositorio.save(compra);

            // Devolver un mensaje de éxito
            response.put("message", "Ingreso registrado exitosamente.");
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
    
    public List<Map<String, Object>> listarDetalle() {
    List<Compra> compras = compraRepositorio.listarConDetalles();
    List<Map<String, Object>> resultado = new ArrayList<>();

    Set<Long> comprasProcesadas = new HashSet<>(); // Set para almacenar los IDs de compras ya procesadas

    for (Compra compra : compras) {
        // Si el ID de la compra ya fue procesado, no volvemos a agregarla
        if (comprasProcesadas.contains(compra.getId())) {
            continue;  // Pasamos a la siguiente compra
        }

        for (DetalleCompra detalle : compra.getDetalles()) {
            Map<String, Object> datosCompra = new HashMap<>();
            datosCompra.put("id", compra.getId());
            datosCompra.put("fecha", compra.getFecha());
            datosCompra.put("proveedor", compra.getProveedor().getNombre());  // Mostramos el proveedor en cada fila
            datosCompra.put("idProveedor", compra.getProveedor().getId());
            datosCompra.put("producto", detalle.getProducto().getDescripcion());  // Mostramos cada producto en una fila
            datosCompra.put("productoCodigo", detalle.getProducto().getCodigo());
            datosCompra.put("cantidad", detalle.getCantidad());  // Cantidad por producto
            datosCompra.put("costo", detalle.getCosto());  // Precio por producto
            datosCompra.put("total", detalle.getTotal());  // Total por producto
            resultado.add(datosCompra);
        }

        // Agregamos el ID de la compra a la lista de compras procesadas
        comprasProcesadas.add(compra.getId());
    }

    // Retornamos la lista de resultados procesados
    return resultado;
}
    
    @Transactional
    public Map<String, Object> eliminarCompra(Long compraId) throws MiException {
        Map<String, Object> response = new HashMap<>();

        try {
            // Buscar la compra por ID
            Compra compra = compraRepositorio.findById(compraId)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada: " + compraId));

            // Eliminar la compra (esto eliminará automáticamente los detalles relacionados)
            compraRepositorio.delete(compra);

            // Responder con éxito
            response.put("message", "Compra eliminada exitosamente.");
        } catch (RuntimeException e) {
            // Capturar errores específicos (venta no encontrada)
            response.put("error", "Error al eliminar la compra: " + e.getMessage());
            // Relanzar la excepción para que el rollback ocurra correctamente
            throw e;
        } catch (Exception e) {
            // Capturar errores generales
            response.put("error", "Error inesperado al eliminar la compra.");
            // Relanzar la excepción para manejar el rollback
            throw new MiException("Error inesperado al eliminar la compra", (DataIntegrityViolationException) e);
        }

        return response;
    }

    public List<Compra> listarCompra() {
    List<Compra> compras =  new ArrayList();
       
       compras = compraRepositorio.findAll();
       
       return compras; 
}
}
