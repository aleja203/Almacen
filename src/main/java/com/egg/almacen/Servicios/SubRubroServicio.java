    
    package com.egg.almacen.Servicios;
    
    import com.egg.almacen.Entidades.Rubro;
    import com.egg.almacen.Entidades.SubRubro;
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
    public class SubRubroServicio {
        
        @Autowired
        SubRubroRepositorio subRubroRepositorio;
        
        
        @Autowired
        RubroRepositorio rubroRepositorio;
    
        @Transactional
        public void crearSubRubro(String nombre, String idRubro) throws MiException {
            validar(nombre, idRubro);
            Rubro rubro = rubroRepositorio.findById(idRubro).get();
            SubRubro subRubro = new SubRubro();
            subRubro.setNombre(nombre);
            subRubro.setRubro(rubro);
            subRubroRepositorio.save(subRubro);
        }
       
    public SubRubro getOne(String id) {
            return subRubroRepositorio.getOne(id);
        }
        
    private void validar(String nombre, String idRubro) throws MiException {
    
            if (nombre == null || nombre.isEmpty()) {
                throw new MiException("El nombre no puede ser nulo o estar vacío");
            }
             
            Rubro rubro = rubroRepositorio.findById(idRubro).orElse(null);
            if (rubro == null) {
                throw new MiException("El rubro seleccionado no es válido.");
            }
    }
    
    public List<SubRubro> listarSubRubros(){
            
           List<SubRubro> subRubros =  new ArrayList();
           
           subRubros = subRubroRepositorio.findAll();
           
           return subRubros;
    }
    
    @Transactional
    public void modificarSubRubro(String nombre, String id, String idRubro)throws MiException{
            System.out.println("idRubro recibido: " + idRubro);
            validar(nombre, idRubro);
            
            Optional<SubRubro> respuesta = subRubroRepositorio.findById(id);
            
            Optional<Rubro> respuestaRubro = rubroRepositorio.findById(idRubro);
            
            Rubro rubro = new Rubro();
            
            if (respuestaRubro.isPresent()) {
                rubro = respuestaRubro.get();
            }
            if (respuesta.isPresent()) {
                
                SubRubro subRubro = respuesta.get();
                
                subRubro.setNombre(nombre);
                
                subRubro.setRubro(rubro);
                
                subRubroRepositorio.save(subRubro);
            }
        }
    
    }
