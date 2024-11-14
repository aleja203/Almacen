
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;

@Transactional
    public void crearCliente(Long dni, String nombre, String email, String domicilio, Long telefono) throws MiException{
        
        validar(dni, nombre, email, domicilio, telefono, false);
        
        Cliente cliente = new Cliente();
        
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        cliente.setEmail(email);
        cliente.setDomicilio(domicilio);
        cliente.setTelefono(telefono);
        
        clienteRepositorio.save(cliente);
        
    }
    
    private void validar(Long dni, String nombre, String email, String domicilio, Long telefono, boolean esModificacion) throws MiException{
        
        if (dni == null) {
            throw new MiException("El DNI no puede ser nulo");
        }
        
        // Verificación de duplicidad de DNI solo si no es modificación
        if (!esModificacion && clienteRepositorio.existsByDni(dni)) {
            throw new MiException("El DNI ya está registrado en la base de datos");
        }
        
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }
//        if (email.isEmpty() || email == null) {
//            throw new MiException("el email no puede ser nulo o estar vacio");
//        }
//        if (domicilio.isEmpty() || domicilio == null ) {
//            throw new MiException("El domicilio no puede estar vacío");
//        }
//
//        if (telefono == null) {
//            throw new MiException("El teléfono no puede ser nulo");
//        }
        
    }
    
    public List<Cliente> listarClientes(){
        
       List<Cliente> clientes =  new ArrayList();
       
       clientes = clienteRepositorio.findAll();
       
       return clientes; 
    }
    
    @Transactional
    public void eliminarCliente(Long dni) throws MiException {

        Cliente cliente = clienteRepositorio.findById(dni).orElseThrow(() -> new MiException("Cliente no encontrado"));

        
        clienteRepositorio.delete(cliente);
    }
    
    public Cliente getOne(Long dni){
       return clienteRepositorio.getOne(dni);
    }
    
    @Transactional
    public void modificarCliente(Long dni, String nombre, String email, String domicilio, Long telefono)throws MiException{
        
        validar(dni, nombre, email, domicilio, telefono, true);
        
        Optional<Cliente> respuesta = clienteRepositorio.findById(dni);
        
        if (respuesta.isPresent()) {
            
            Cliente cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setEmail(email);
            cliente.setDomicilio(domicilio);
            cliente.setTelefono(telefono);
            
            clienteRepositorio.save(cliente);
        }
    }
}
