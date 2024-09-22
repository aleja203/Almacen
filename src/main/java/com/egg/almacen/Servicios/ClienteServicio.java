
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
    public void crearCliente(Long dni, String nombre) throws MiException{
        
        validar(dni, nombre);
        
        Cliente cliente = new Cliente();
        
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        
        clienteRepositorio.save(cliente);
        
    }
    
    private void validar(Long dni, String nombre) throws MiException{
        
        if (dni == null) {
            throw new MiException("El DNI no puede ser nulo");
        }
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }
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
    public void modificarCliente(Long dni, String nombre)throws MiException{
        
        validar(dni, nombre);
        
        Optional<Cliente> respuesta = clienteRepositorio.findById(dni);
        
        if (respuesta.isPresent()) {
            
            Cliente cliente = respuesta.get();
            
            cliente.setNombre(nombre);
            
            clienteRepositorio.save(cliente);
        }
    }
}
