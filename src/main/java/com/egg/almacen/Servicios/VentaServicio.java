
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.VentaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaServicio {
    
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private VentaRepositorio ventaRepositorio;
    @Autowired
    private MovimientoServicio movimientoServicio;
    
   
    @Transactional
    public void crearVenta(String idProducto, Integer cantidad, Double precioDeVenta, String detalle) throws MiException{
        
        
        validar(idProducto, cantidad, precioDeVenta);
        
        Producto producto = productoRepositorio.findById(idProducto).get();
        
        Venta venta = new Venta();
        
        venta.setFecha(new Date());
        venta.setProducto(producto);
        venta.setCantidad(cantidad);
        venta.setPrecioVenta(precioDeVenta);
        venta.setDetalle(detalle);
        
        ventaRepositorio.save(venta);
        
        movimientoServicio.registrarMovimiento(producto, cantidad, precioDeVenta, "VENTA", detalle);
    }
    
    private void validar(String idProducto, Integer cantidad, Double precioDeVenta) throws MiException{
        
        if (cantidad == null) {
            throw new MiException("Cantidad no puede ser nulo");
        } else {
        }
        if (precioDeVenta == null) {
            throw new MiException("Precio de venta inválido");
        }
        // Validación adicional para asegurar que se seleccionó un Producto válido
        Producto Producto = productoRepositorio.findById(idProducto).orElse(null);
        if (Producto == null) {
            throw new MiException("El Producto seleccionado no es válido.");
        }
    }
    
    public List<Venta> listarVentas(){
        
       List<Venta> ventas =  new ArrayList();
       
       ventas = ventaRepositorio.findAll();
       
       return ventas; 
    }
    
    public Venta getOne(String id){
       return ventaRepositorio.getOne(id);
    }
    
}
