
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.RubroRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RubroServicio {

    @Autowired
    RubroRepositorio rubroRepositorio;
    
    @Autowired
    ProductoRepositorio productoRepositorio;

    @Transactional
    public void crearRubro(String nombre) throws MiException {

        validar(nombre);
        Rubro rubro = new Rubro();

        rubro.setNombre(nombre);

        rubroRepositorio.save(rubro);

    }

    public Rubro getOne(String id) {
        return rubroRepositorio.getOne(id);
    }
    private void validar(String nombre) throws MiException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre del rubro no puede ser nulo o estar vacío");
        }
    }
    @Transactional
    public void eliminarRubro(String id) throws MiException {
        
            Rubro rubro = rubroRepositorio.findById(id).orElseThrow(() -> new MiException("Rubro no encontrado"));

            long productosAsociados = productoRepositorio.countByRubro(rubro);
            if (productosAsociados > 0) {
                throw new MiException("No se puede eliminar el rubro porque tiene productos asociados.");
            }
            rubroRepositorio.delete(rubro);
    }
    
    public List<Rubro> listarRubros(){
        
       List<Rubro> rubros =  new ArrayList();
       
       rubros = rubroRepositorio.findAll();
       
       return rubros;
}
    
    @Transactional
    public void modificarRubro(String nombre, String id)throws MiException{
        
        validar(nombre);
        
        Optional<Rubro> respuesta = rubroRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            
            Rubro rubro = respuesta.get();
            rubro.setNombre(nombre);
            rubroRepositorio.save(rubro);
        }
    }
}
