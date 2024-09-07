
package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.RubroRepositorio;
import com.egg.almacen.Servicios.RubroServicio;
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
@RequestMapping("/rubro")
public class RubroControlador {
    
    @Autowired
    private RubroServicio rubroServicio;
    
    @Autowired
    private RubroRepositorio rubroRepositorio;
    
    @GetMapping("/registrar")
    public String registrar(){
        return "rubro_form.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {

        try {
            rubroServicio.crearRubro(nombre);

            modelo.put("exito", "El rubro fue creado correctamente!");
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "rubro_form.html";
        }

        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        
        List <Rubro> rubros = rubroServicio.listarRubros();
        
        modelo.addAttribute("rubros", rubros);
        
        return "rubro_list.html";
        
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("rubro", rubroServicio.getOne(id));
        
        return "rubro_modificar.html";
    }
    
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombre, ModelMap modelo, RedirectAttributes redirectAttributes) {
        try {
            
            rubroServicio.modificarRubro(nombre, id);
            
            Optional<Rubro> rubroOptional = rubroRepositorio.findById(id);
            
            modelo.put("rubro", rubroOptional.get());
            redirectAttributes.addFlashAttribute("exito", "Rubro actualizado exitosamente");
            
            return "redirect:../lista";
        } catch (MiException ex) {
            
             Optional<Rubro> rubroOptional = rubroRepositorio.findById(id);
                        
            modelo.put("rubro", rubroOptional.get());
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("id", id);
            modelo.addAttribute("nombre", nombre);
            return "rubro_modificar.html";
        }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarRubro(@PathVariable String id, RedirectAttributes redirectAttributes) throws MiException {
        try {
            rubroServicio.eliminarRubro(id);
            redirectAttributes.addFlashAttribute("exito", "El rubro ha sido eliminado con exitosamente.");
        }
        catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/rubro/lista";
    }
    
}
