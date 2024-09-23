
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Entidades.SubRubro;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.RubroRepositorio;
import com.egg.almacen.Repositorios.SubRubroRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServicio {

    @Autowired
    private RubroRepositorio rubroRepositorio;
    
    @Autowired
    private SubRubroRepositorio subRubroRepositorio;
    
    @Autowired
    private ProductoRepositorio productoRepositorio;
    
        
    @Transactional
    public void crearProducto(String codigo,  String descripcion, String idRubro, String idSubRubro, Double costo, Double precioVenta) throws MiException{
        
        
        validar(codigo, descripcion, idRubro, idSubRubro);
        
                
        Rubro rubro = rubroRepositorio.findById(idRubro).get();
        SubRubro subRubro = subRubroRepositorio.findById(idSubRubro).get();
        

        Producto producto = new Producto();
        
        producto.setAlta(new Date());
        producto.setCodigo(codigo);
        producto.setDescripcion(descripcion);
        producto.setRubro(rubro);
        producto.setSubRubro(subRubro);
        producto.setCosto(costo);
        producto.setPrecioVenta(precioVenta);
        
        productoRepositorio.save(producto);
        
    }
    
    public List<Producto> listarProductos(){
        
       List<Producto> productos =  new ArrayList();
       
       productos = productoRepositorio.findAll();
       
       return productos; 
    }
    
    @Transactional
    public void modificarProducto(String codigo, String descripcion, String idRubro, String idSubRubro, Double costo, Double precioVenta)throws MiException{
        
        validar(codigo, descripcion, idRubro, idSubRubro);
        
        Optional<Producto> respuesta = productoRepositorio.findById(codigo);
        Optional<Rubro> respuestaRubro = rubroRepositorio.findById(idRubro);
        Optional<SubRubro> respuestaSubRubro = subRubroRepositorio.findById(idSubRubro);
        
        Rubro Rubro = new Rubro();
        SubRubro SubRubro = new SubRubro();
        
        if (respuestaRubro.isPresent()) {
            Rubro = respuestaRubro.get();
        }
        
        if (respuestaSubRubro.isPresent()) {
            SubRubro = respuestaSubRubro.get();
        }
        
        if (respuesta.isPresent()) {
            
            Producto producto = respuesta.get();
            
            producto.setDescripcion(descripcion);
            
            producto.setCosto(costo);
            
            producto.setPrecioVenta(precioVenta);
            
            producto.setRubro(Rubro);
            
            producto.setSubRubro(SubRubro);
            
            
            productoRepositorio.save(producto);
        }
    }
    
    public Producto getOne(String codigo){
       return productoRepositorio.getOne(codigo);
    }
    
        private void validar(String codigo, String descripcion, String idRubro, String idSubRubro) throws MiException{
        
        if (codigo == null) {
            throw new MiException("El codigo no puede ser nulo");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("El descripcion no puede ser nulo o estar vacío");
        }

        // Validación adicional para asegurar que se seleccionó un Rubro válido
        Rubro Rubro = rubroRepositorio.findById(idRubro).orElse(null);
        if (Rubro == null) {
            throw new MiException("El Rubro seleccionado no es válido.");
        }
        // Validación adicional para asegurar que se seleccionó una SubRubro válida
        SubRubro subRubro = subRubroRepositorio.findById(idSubRubro).orElse(null);
        if (subRubro == null) {
            throw new MiException("El SubRubro seleccionado no es válido.");
        }
    }
    
    @Transactional
    public void eliminarProducto(String codigo) throws MiException {

        Producto producto = productoRepositorio.findById(codigo).orElseThrow(() -> new MiException("Producto no encontrado"));

        
        productoRepositorio.delete(producto);
    }
        
}
