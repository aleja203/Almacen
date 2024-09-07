
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Movimiento;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Repositorios.MovimientoRepositorio;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.ProveedorRepositorio;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimientoServicio {
    
    @Autowired
    private MovimientoRepositorio movimientoRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;
     @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    
    @Transactional
    public void registrarMovimiento(Producto producto, Integer cantidad, Double valor, String tipo, String detalle) {
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setCantidad(cantidad);
        movimiento.setValor(valor);
        movimiento.setTipo(tipo);
        movimiento.setDetalle(detalle);
        movimiento.setFecha(new Date());

        // Persistir el movimiento
        movimientoRepositorio.save(movimiento);
        
        if (tipo.equals("VENTA")) {
            producto.setStock(producto.getStock() - cantidad);
        } else if (tipo.equals("INGRESO")) {
            producto.setStock(producto.getStock() + cantidad);
        }
        productoRepositorio.save(producto);
    }
    
    @Transactional
    public void registrarMovimiento(Producto producto, Integer cantidad, Double valor, String tipo, String detalle, Proveedor proveedor) {
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setCantidad(cantidad);
        movimiento.setValor(valor);
        movimiento.setTipo(tipo);
        movimiento.setDetalle(detalle);
        movimiento.setFecha(new Date());
        movimiento.setProveedor(proveedor);

        // Persistir el movimiento
        movimientoRepositorio.save(movimiento);
        
        if (tipo.equals("VENTA")) {
            producto.setStock(producto.getStock() - cantidad);
        } else if (tipo.equals("INGRESO")) {
            producto.setStock(producto.getStock() + cantidad);
        }
        productoRepositorio.save(producto);
    }
     
}
