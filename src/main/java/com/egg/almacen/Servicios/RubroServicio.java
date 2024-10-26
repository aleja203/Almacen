
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.RubroRepositorio;
import com.egg.almacen.Repositorios.SubRubroRepositorio;
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

    @Autowired
    SubRubroRepositorio subRubroRepositorio;
    
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
    // Buscar el rubro por id
    Rubro rubro = rubroRepositorio.findById(id).orElseThrow(() -> new MiException("Rubro no encontrado"));

    // Verificar si el rubro tiene subrubros asociados
    if (subRubroRepositorio.existsByRubroId(id)) {
        throw new MiException("El rubro no se puede eliminar porque tiene subrubros asociados.");
    }

    // Verificar si el rubro tiene productos asociados
    if (productoRepositorio.existsByRubroId(id)) {
        throw new MiException("El rubro no se puede eliminar porque tiene productos asociados.");
    }

    // Si no tiene asociaciones, eliminar el rubro
    rubroRepositorio.delete(rubro);
    System.out.println("Estoy en el servicio");
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
