
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.ProveedorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorServicio {
    
    @Autowired
    ProveedorRepositorio proveedorRepositorio;
    
    @Autowired
    ProductoRepositorio productoRepositorio;

    @Transactional
    public void crearProveedor(String nombre) throws MiException {

        validar(nombre);
        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(nombre);

        proveedorRepositorio.save(proveedor);

    }

    public Proveedor getOne(String id) {
        return proveedorRepositorio.getOne(id);
    }
    private void validar(String nombre) throws MiException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre del proveedor no puede ser nulo o estar vacío");
        }
    }
    @Transactional
    public void eliminarProveedor(String id) throws MiException {
        
            Proveedor proveedor = proveedorRepositorio.findById(id).orElseThrow(() -> new MiException("Proveedor no encontrado"));

            proveedorRepositorio.delete(proveedor);
    }
    
    public List<Proveedor> listarProveedores(){
        
       List<Proveedor> proveedores =  new ArrayList();
       
       proveedores = proveedorRepositorio.findAll();
       
       return proveedores;
}
    @Transactional
    public void modificarProveedor(String nombre, String id)throws MiException{
        
        validar(nombre);
        
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            
            Proveedor proveedor = respuesta.get();
            proveedor.setNombre(nombre);
            proveedorRepositorio.save(proveedor);
        }
    }
    
}
