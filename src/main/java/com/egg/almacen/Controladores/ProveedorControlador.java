
package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ProveedorRepositorio;
import com.egg.almacen.Servicios.ProveedorServicio;
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
@RequestMapping("/proveedor")
public class ProveedorControlador {
  
    @Autowired
    private ProveedorServicio proveedorServicio;
    
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    
    @GetMapping("/registrar")
    public String registrar(){
        return "proveedor_form.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {

        try {
            proveedorServicio.crearProveedor(nombre);

            modelo.put("exito", "El proveedor fue creado correctamente!");
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "proveedor_form.html";
        }

        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        
        List <Proveedor> proveedores = proveedorServicio.listarProveedores();
        
        modelo.addAttribute("proveedores", proveedores);
        
        return "proveedor_list.html";
        
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("proveedor", proveedorServicio.getOne(id));
        
        return "proveedor_modificar.html";
    }
    
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombre, ModelMap modelo, RedirectAttributes redirectAttributes) {
        try {
            
            proveedorServicio.modificarProveedor(nombre, id);
            
            Optional<Proveedor> proveedorOptional = proveedorRepositorio.findById(id);
            
            modelo.put("proveedor", proveedorOptional.get());
            redirectAttributes.addFlashAttribute("exito", "Proveedor actualizado exitosamente");
            
            return "redirect:../lista";
        } catch (MiException ex) {
            
             Optional<Proveedor> proveedorOptional = proveedorRepositorio.findById(id);
                        
            modelo.put("proveedor", proveedorOptional.get());
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("id", id);
            modelo.addAttribute("nombre", nombre);
            return "proveedor_modificar.html";
        }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable String id, RedirectAttributes redirectAttributes) throws MiException {
        try {
            proveedorServicio.eliminarProveedor(id);
            redirectAttributes.addFlashAttribute("exito", "El proveedor ha sido eliminado con exitosamente.");
        }
        catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/proveedor/lista";
    }
    
}



