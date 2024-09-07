
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.IngresoDeMercaderia;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.IngresoDeMercaderiaRepositorio;
import com.egg.almacen.Repositorios.MovimientoRepositorio;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
   
    @Transactional
    public void crearIngresoDeMercaderia(String idProducto, Integer cantidad, Double costo, String detalle) throws MiException{
        
        validar(idProducto, cantidad, costo);
        
        Producto producto = productoRepositorio.findById(idProducto).get();
        
        IngresoDeMercaderia ingresoDeMercaderia = new IngresoDeMercaderia();
        
        ingresoDeMercaderia.setFecha(new Date());
        ingresoDeMercaderia.setProducto(producto);
        ingresoDeMercaderia.setCantidad(cantidad);
        ingresoDeMercaderia.setCosto(costo);
        ingresoDeMercaderia.setDetalle(detalle);
        
        ingresoDeMercaderiaRepositorio.save(ingresoDeMercaderia);
        
        movimientoServicio.registrarMovimiento(producto, cantidad, costo, "INGRESO", detalle);
        
    }
    
    private void validar(String idProducto, Integer cantidad, Double costo) throws MiException{
        
        if (cantidad == null) {
            throw new MiException("Cantidad no puede ser nulo");
        } else {
        }
        if (costo == null) {
            throw new MiException("Precio de venta inválido");
        }
        // Validación adicional para asegurar que se seleccionó un Producto válido
        Producto Producto = productoRepositorio.findById(idProducto).orElse(null);
        if (Producto == null) {
            throw new MiException("El Producto seleccionado no es válido.");
        }
    }
    
    public List<IngresoDeMercaderia> listarIngresoDeMercaderia(){
        
       List<IngresoDeMercaderia> ingresoDeMercaderia =  new ArrayList();
       
       ingresoDeMercaderia = ingresoDeMercaderiaRepositorio.findAll();
       
       return ingresoDeMercaderia; 
    }
    
}
