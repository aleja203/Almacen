
package com.egg.almacen.Servicios;


import com.egg.almacen.DTO.VentaDTO;
import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.DetalleVenta;
import com.egg.almacen.Entidades.FormaDePago;
import com.egg.almacen.Entidades.FormaPagoVenta;
import com.egg.almacen.Entidades.Movimiento;
import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import com.egg.almacen.Repositorios.DetalleVentaRepositorio;
import com.egg.almacen.Repositorios.FormaDePagoRepositorio;
import com.egg.almacen.Repositorios.MovimientoRepositorio;
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
import javax.persistence.EntityNotFoundException;
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
    private MovimientoRepositorio movimientoRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
        @Autowired
    private FormaDePagoRepositorio formaDePagoRepositorio;

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
                        
                        // Crear y guardar un movimiento para este detalle
                        Movimiento movimiento = new Movimiento();
                        movimiento.setFecha(new Date());
                        movimiento.setTipo("VENTA");
                        movimiento.setProvCli(venta.getCliente().getNombre());
                        
                        // Aquí guardamos la venta antes para tener su ID
                        ventaRepositorio.save(venta);
                        
                        // Asegúrate de que el movimiento use el ID de la venta
                        movimiento.setFactura(venta.getId());  // Establecer el ID de la venta
                        
                        // Establecer el nombre del producto (descripción) en el movimiento
                        movimiento.setProducto(producto.getDescripcion());  // Cambiar de código a descripción
                        movimiento.setCantidad(detalleDTO.getCantidad());
                        movimiento.setPrecio(detalleDTO.getPrecioVenta());
                        movimientoRepositorio.save(movimiento);

                    } else {
                        throw new RuntimeException("Producto no encontrado: " + detalleDTO.getProducto());
                    }

                    detalle.setVenta(venta);  // Establecer la relación bidireccional
                    return detalle;
                })
                .collect(Collectors.toSet());

        venta.setDetalles(detalles);

        // Manejar las formas de pago
        Set<FormaPagoVenta> formasPago = ventaDTO.getFormasPago().stream()
                .map(formaPagoDTO -> {
                    FormaPagoVenta formaPagoVenta = new FormaPagoVenta();

                    System.out.println("Tipo en formaPagoDTO: " + formaPagoDTO.getTipoPago());
                    System.out.println("FormaPago en formaPagoDTO: " + formaPagoDTO.getFormaDePago());

                    formaPagoVenta.setTipoPago(formaPagoDTO.getTipoPago());

                    // Verificar si el id de FormaDePago no es nulo antes de buscar en la base de datos
                    if (formaPagoDTO.getFormaDePago() == null) {
                        throw new IllegalArgumentException("FormaDePagoId no puede ser null para tipo de pago " + formaPagoDTO.getTipoPago());
                    }

                    // Obtener FormaDePago desde la base de datos
                    FormaDePago formaDePago = formaDePagoRepositorio.findById(formaPagoDTO.getFormaDePago())
                            .orElseThrow(() -> new EntityNotFoundException("FormaDePago no encontrada con id: " + formaPagoDTO.getFormaDePago()));

                    formaPagoVenta.setFormaDePago(formaDePago); // Asignar la forma de pago persistente a formaPagoVenta
                    formaPagoVenta.setImporte(formaPagoDTO.getImporte());
                    formaPagoVenta.setVenta(venta); // Establecer la relación con la venta
                    return formaPagoVenta;
                })
                .collect(Collectors.toSet());

        venta.setFormasPago(formasPago); // Asignar las formas de pago a la venta

        

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

    public List<Map<String, Object>> listarFormaPago() {
    // Obtener la lista de ventas desde el repositorio
    List<Venta> ventas = ventaRepositorio.listarConFormasPago();
    
    // Imprimir la cantidad de ventas obtenidas
    System.out.println("Total de ventas obtenidas: " + ventas.size());

    // Inicializar la lista para almacenar los resultados
    List<Map<String, Object>> resultado = new ArrayList<>();
    Set<Long> ventasProcesadas = new HashSet<>(); // Set para almacenar los IDs de ventas ya procesadas

    for (Venta venta : ventas) {
        // Si el ID de la venta ya fue procesado, no volvemos a agregarla
        if (ventasProcesadas.contains(venta.getId())) {
            continue;  // Pasamos a la siguiente venta
        }

        // Imprimir detalles de la venta
        System.out.println("Procesando venta ID: " + venta.getId() + ", Fecha: " + venta.getFecha() + ", Cliente: " + venta.getCliente().getNombre());

        for (FormaPagoVenta formaPago : venta.getFormasPago()) {
            Map<String, Object> datosVenta = new HashMap<>();
            datosVenta.put("id", venta.getId());
            datosVenta.put("fecha", venta.getFecha());
            datosVenta.put("cliente", venta.getCliente().getNombre());  // Mostramos el cliente en cada fila
            datosVenta.put("clienteDni", venta.getCliente().getDni());
            datosVenta.put("formaPago", formaPago.getFormaDePago().getDescripcion());
            datosVenta.put("tipoPago", formaPago.getTipoPago());
            datosVenta.put("importe", formaPago.getImporte());

            // Imprimir detalles de la forma de pago
            System.out.println("  Forma de Pago: " + formaPago.getFormaDePago() + ", Tipo: " + formaPago.getTipoPago() + ", Importe: " + formaPago.getImporte() + ", Id de la forma de pago: " + formaPago.getId());

            resultado.add(datosVenta);
        }

        // Agregamos el ID de la venta a la lista de ventas procesadas
        ventasProcesadas.add(venta.getId());
    }

    return resultado;
}

    
    @Transactional
    public Map<String, Object> eliminarVenta(Long ventaId) throws MiException {
        Map<String, Object> response = new HashMap<>();

        try {
            // Buscar la venta por ID
            Venta venta = ventaRepositorio.findById(ventaId)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + ventaId));
            
            // Obtener todos los movimientos relacionados con esta venta por su factura (ID de venta)
            List<Movimiento> movimientos = movimientoRepositorio.findByFactura(ventaId);
        
            // Eliminar todos los movimientos asociados a la venta
            for (Movimiento movimiento : movimientos) {
                if ("VENTA".equals(movimiento.getTipo())) { // Comprobar si el tipo es "VENTA"
                    movimientoRepositorio.delete(movimiento);
                }
        }

            // Iterar sobre los detalles de la venta antes de eliminarla
            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto producto = detalle.getProducto();
                Double cantidadVendida = detalle.getCantidad();

                // Sumar la cantidad vendida de nuevo al stock del producto
                producto.setExistencia(producto.getExistencia() + cantidadVendida);

                // Guardar el cambio de stock en el repositorio de productos
                productoRepositorio.save(producto);
            }

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

    
