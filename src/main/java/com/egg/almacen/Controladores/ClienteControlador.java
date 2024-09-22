
package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import com.egg.almacen.Servicios.ClienteServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

//@Autowired
//    private ClienteServicio clienteServicio;
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ClienteServicio clienteServicio;
    
    @GetMapping("/registrar")
    public String registrar(){
        return "cliente_form.html";
    }
    
        @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long dni, 
                           @RequestParam String nombre,
                           ModelMap modelo) {

        try {

            clienteServicio.crearCliente(dni, nombre);

            modelo.put("exito", "El cliente fue cargado correctamente!");

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "cliente_form.html";
        }
        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        
        List <Cliente> clientes = clienteServicio.listarClientes();
        
        modelo.addAttribute("clientes", clientes);
        
        return "cliente_list.html";
        
    }
    
     @GetMapping("/eliminar/{dni}")
    public String eliminarProducto(@PathVariable Long dni, RedirectAttributes redirectAttributes) {
        try {
            clienteServicio.eliminarCliente(dni);
            redirectAttributes.addFlashAttribute("exito", "El cliente ha sido eliminado exitosamente.");
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/lista";
    }
    
    @GetMapping("/modificar/{dni}")
    public String modificar(@PathVariable Long dni, ModelMap modelo){
        modelo.put("cliente", clienteServicio.getOne(dni));
        
        return "cliente_modificar.html";
    }
    
    @PostMapping("/modificar/{dni}")
    public String modificar(@PathVariable Long dni,
                            @RequestParam String nombre,
                            ModelMap modelo,
                            RedirectAttributes redirectAttributes) {
        try {
            
            Optional<Cliente> clienteOptional = clienteRepositorio.findById(dni);
            
            modelo.put("producto", clienteOptional.get());
            redirectAttributes.addFlashAttribute("exito", "Cliente actualizado exitosamente");
            
            clienteServicio.modificarCliente(dni, nombre);
            
            return "redirect:../lista";
        } catch (MiException ex) {
            
            Optional<Cliente> clienteOptional = clienteRepositorio.findById(dni);
                        
            modelo.put("cliente", clienteOptional.get());
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("id", dni);
            modelo.addAttribute("nombre", nombre);
            return "cliente_modificar.html";
        }
    }
    
}
