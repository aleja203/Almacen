
package com.egg.almacen.Servicios;


import com.egg.almacen.DTO.VentaDTO;
import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.CuentaCorriente;
import com.egg.almacen.Entidades.DetalleVenta;
import com.egg.almacen.Entidades.FormaDePago;
import com.egg.almacen.Entidades.FormaPagoVenta;
import com.egg.almacen.Entidades.Movimiento;
import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import com.egg.almacen.Repositorios.CuentaCorrienteRepositorio;
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
    @Autowired
    private CuentaCorrienteRepositorio cuentaCorrienteRepositorio;

@Transactional
public Map<String, Object> crearVenta(VentaDTO ventaDTO) {
    Map<String, Object> response = new HashMap<>();

    try {
        Venta venta = new Venta();
        final Cliente cliente;

        if (ventaDTO.getClienteId() != null) {
            cliente = clienteRepositorio.findById(ventaDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + ventaDTO.getClienteId()));
            venta.setCliente(cliente);
        } else {
            cliente = null;
        }

        venta.setObservaciones(ventaDTO.getObservaciones());
        venta.setTotalVenta(ventaDTO.getTotalVenta());
        venta.setFecha(new Date());

        Set<DetalleVenta> detalles = ventaDTO.getDetalles().stream()
                .map(detalleDTO -> {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setCantidad(detalleDTO.getCantidad());
                    detalle.setPrecioVenta(detalleDTO.getPrecioVenta());
                    detalle.setTotal(detalleDTO.getTotal());

                    Producto producto = productoRepositorio.findByCodigo(detalleDTO.getProducto());
                    if (producto != null) {
                        producto.setExistencia(producto.getExistencia() - detalleDTO.getCantidad());
                        productoRepositorio.save(producto);

                        detalle.setProducto(producto);

                        Movimiento movimiento = new Movimiento();
                        CuentaCorriente cuentaCorriente = new CuentaCorriente();

                        movimiento.setFecha(new Date());
                        movimiento.setTipo("VENTA");
                        movimiento.setProvCli(cliente != null ? cliente.getNombre() : "Cliente no asignado");

                        // cuentaCorriente.setFecha(new Date());
                        cuentaCorriente.setTipo("VENTA");
                        
                        cuentaCorriente.setCliente(cliente != null ? cliente : null);

                        ventaRepositorio.save(venta);

                        movimiento.setFactura(venta.getId());
                        cuentaCorriente.setFacturaRecibo(venta.getId());

                        movimiento.setProducto(producto.getDescripcion());
                        // Si el movimiento es una venta, la cantidad se guarda como negativa
                        movimiento.setCantidad("VENTA".equalsIgnoreCase(movimiento.getTipo())
                                ? -detalleDTO.getCantidad()
                                : detalleDTO.getCantidad());

                        
                        movimiento.setPrecio(detalleDTO.getPrecioVenta());
                        movimientoRepositorio.save(movimiento);

                    } else {
                        throw new RuntimeException("Producto no encontrado: " + detalleDTO.getProducto());
                    }

                    detalle.setVenta(venta);
                    return detalle;
                })
                .collect(Collectors.toSet());

        venta.setDetalles(detalles);

        Set<FormaPagoVenta> formasPago = ventaDTO.getFormasPago().stream()
                .map(formaPagoDTO -> {
                    FormaPagoVenta formaPagoVenta = new FormaPagoVenta();
                    formaPagoVenta.setTipoPago(formaPagoDTO.getTipoPago());

                    if (formaPagoDTO.getFormaDePago() == null) {
                        throw new IllegalArgumentException("FormaDePagoId no puede ser null para tipo de pago " + formaPagoDTO.getTipoPago());
                    }

                    FormaDePago formaDePago = formaDePagoRepositorio.findById(formaPagoDTO.getFormaDePago())
                            .orElseThrow(() -> new EntityNotFoundException("FormaDePago no encontrada con id: " + formaPagoDTO.getFormaDePago()));

                    formaPagoVenta.setFormaDePago(formaDePago);
                    formaPagoVenta.setImporte(formaPagoDTO.getImporte());
                    formaPagoVenta.setVenta(venta);

                    System.out.println("Valor de tipoPago en formaPagoDTO: " + formaPagoDTO.getTipoPago());

                    if ("CUENTA_CORRIENTE".equals(formaPagoDTO.getTipoPago().toString())) {
                        CuentaCorriente cuentaCorriente = new CuentaCorriente();
                        //cuentaCorriente.setImporte(formaPagoDTO.getImporte());
                        // Asegurarnos de que el importe se almacene como negativo
                        double importeNegativo = formaPagoDTO.getImporte() > 0 ? -formaPagoDTO.getImporte() : formaPagoDTO.getImporte();
                        cuentaCorriente.setImporte(importeNegativo);
                        cuentaCorriente.setFecha(new Date());
                        cuentaCorriente.setTipo("VENTA");
                        
                        cuentaCorriente.setCliente(cliente != null ? cliente : null);
                        cuentaCorriente.setFacturaRecibo(venta.getId());

                        // Actualizar saldo del cliente y guardar en repositorio
                        cliente.setSaldo(cliente.getSaldo() - formaPagoDTO.getImporte());
                        
                        clienteRepositorio.save(cliente);
                        cuentaCorrienteRepositorio.save(cuentaCorriente);
                    }

                    return formaPagoVenta;
                })
                .collect(Collectors.toSet());

        venta.setFormasPago(formasPago);

        ventaRepositorio.save(venta);

        response.put("message", "Venta registrada exitosamente.");
        return response;
    } catch (RuntimeException e) {
        response.put("error", "Error al registrar la venta: " + e.getMessage());
        return response;
    } catch (Exception e) {
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
       
       ventas = ventaRepositorio.findAllByOrderByFechaDesc();
       
       return ventas; 
}
    
    
    
}

    
