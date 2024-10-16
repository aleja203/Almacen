
package com.egg.almacen.Servicios;


import com.egg.almacen.DTO.DetalleVentaDTO;
import com.egg.almacen.DTO.VentaDTO;
import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.DetalleVenta;
import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import com.egg.almacen.Repositorios.DetalleVentaRepositorio;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.VentaRepositorio;
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
public class VentaServicio {
    
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private VentaRepositorio ventaRepositorio;
    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;
    @Autowired
    private MovimientoServicio movimientoServicio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Transactional
    public Map<String, Object> crearVenta(VentaDTO ventaDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            Venta venta = new Venta();

            // Verificar si el clienteId no es nulo ni vacío
            if (ventaDTO.getClienteId() != null) {
                // Buscar el cliente por su ID
                Cliente cliente = clienteRepositorio.findById(ventaDTO.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + ventaDTO.getClienteId()));
                System.out.println("El cliente es: " + ventaDTO.getClienteId());
                // Asignar el cliente encontrado
                venta.setCliente(cliente);
            }

            // Continuar con la asignación de los demás campos de la venta
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
                            
                         producto.setExistencia(producto.getExistencia() - detalleDTO.getCantidad());

                        // Guardar el producto con la nueva existencia
                        productoRepositorio.save(producto);   
                            
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

            // Devolver un mensaje de éxito
            response.put("message", "Venta registrada exitosamente.");
            return response;
        } catch (RuntimeException e) {
            // Captura de errores específicos (Producto no encontrado o Cliente no encontrado)
            response.put("error", "Error al registrar la venta: " + e.getMessage());
            return response;
        } catch (Exception e) {
            // Captura de otros errores generales
            response.put("error", "Error inesperado al registrar la venta.");
            return response;
        }
    }

    public List<Map<String, Object>> listarDetalle() {
    List<Venta> ventas = ventaRepositorio.listarConDetalles();
    List<Map<String, Object>> resultado = new ArrayList<>();

    Set<Long> ventasProcesadas = new HashSet<>(); // Set para almacenar los IDs de ventas ya procesadas

    for (Venta venta : ventas) {
        // Si el ID de la venta ya fue procesado, no volvemos a agregarla
        if (ventasProcesadas.contains(venta.getId())) {
            continue;  // Pasamos a la siguiente venta
        }

        for (DetalleVenta detalle : venta.getDetalles()) {
            Map<String, Object> datosVenta = new HashMap<>();
            datosVenta.put("id", venta.getId());
            datosVenta.put("fecha", venta.getFecha());
            datosVenta.put("cliente", venta.getCliente().getNombre());  // Mostramos el cliente en cada fila
            datosVenta.put("dniCliente", venta.getCliente().getDni());
            datosVenta.put("producto", detalle.getProducto().getDescripcion());  // Mostramos cada producto en una fila
            datosVenta.put("productoCodigo", detalle.getProducto().getCodigo());
            datosVenta.put("cantidad", detalle.getCantidad());  // Cantidad por producto
            datosVenta.put("precioVenta", detalle.getPrecioVenta());  // Precio por producto
            datosVenta.put("total", detalle.getTotal());  // Total por producto
            resultado.add(datosVenta);
        }

        // Agregamos el ID de la venta a la lista de ventas procesadas
        ventasProcesadas.add(venta.getId());
    }

    // Retornamos la lista de resultados procesados
    return resultado;
}

    @Transactional
    public Map<String, Object> eliminarVenta(Long ventaId) throws MiException {
        Map<String, Object> response = new HashMap<>();

        try {
            // Buscar la venta por ID
            Venta venta = ventaRepositorio.findById(ventaId)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + ventaId));

            // Eliminar la venta (esto eliminará automáticamente los detalles relacionados)
            ventaRepositorio.delete(venta);

            // Responder con éxito
            response.put("message", "Venta eliminada exitosamente.");
        } catch (RuntimeException e) {
            // Capturar errores específicos (venta no encontrada)
            response.put("error", "Error al eliminar la venta: " + e.getMessage());
            // Relanzar la excepción para que el rollback ocurra correctamente
            throw e;
        } catch (Exception e) {
            // Capturar errores generales
            response.put("error", "Error inesperado al eliminar la venta.");
            // Relanzar la excepción para manejar el rollback
            throw new MiException("Error inesperado al eliminar la venta", (DataIntegrityViolationException) e);
        }

        return response;
    }

    public List<Venta> listarVenta() {
    List<Venta> ventas =  new ArrayList();
       
       ventas = ventaRepositorio.findAll();
       
       return ventas; 
}
    
    
    
}

    
