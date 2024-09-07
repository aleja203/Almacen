
package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Entidades.SubRubro;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.RubroRepositorio;
import com.egg.almacen.Repositorios.SubRubroRepositorio;
import com.egg.almacen.Servicios.RubroServicio;
import com.egg.almacen.Servicios.SubRubroServicio;
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
@RequestMapping("/subRubro")
public class SubRubroControlador {
    
    @Autowired
    private SubRubroServicio subRubroServicio;
    @Autowired
    private RubroServicio rubroServicio;    
    @Autowired
    private RubroRepositorio rubroRepositorio;
    @Autowired
    private SubRubroRepositorio subRubroRepositorio;
    
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Rubro> rubros = rubroServicio.listarRubros();
        modelo.addAttribute("rubros", rubros);
        
        return "SubRubro_form.html";
    }
   
    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) String nombre, String idRubro,  ModelMap modelo) {

        try {

            subRubroServicio.crearSubRubro(nombre, idRubro);

            modelo.put("exito", "El subrubro fue cargado correctamente!");

        } catch (MiException ex) {
            List<Rubro> rubros = rubroServicio.listarRubros();

            // Imprimir la lista de rubros en la consola para depuración
            if (rubros != null && !rubros.isEmpty()) {
                System.out.println("Lista de rubros:");
                for (Rubro rubro : rubros) {
                    System.out.println("ID: " + rubro.getId() + ", Nombre: " + rubro.getNombre());
                }
            } else {
                System.out.println("La lista de rubros está vacía o es nula.");
            }
            
            modelo.addAttribute("rubros", rubros);

            modelo.put("error", ex.getMessage());
            return "subRubro_form.html";
        }
        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<SubRubro> subRubros = subRubroServicio.listarSubRubros();
        modelo.addAttribute("subRubros", subRubros);
        List<Rubro> rubros = rubroServicio.listarRubros();
        modelo.addAttribute("rubros", rubros);

        return "subRubro_list";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("subRubro", subRubroServicio.getOne(id));
        
        List<Rubro> rubros = rubroServicio.listarRubros();
        modelo.addAttribute("rubros", rubros);
        return "subRubro_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombre, @RequestParam String idRubro, ModelMap modelo, RedirectAttributes redirectAttributes) {
        try {
            
            List<Rubro> rubros = rubroServicio.listarRubros();
            System.out.println("idRubro recibido: " + idRubro);           
            Optional<SubRubro> subRubroOptional = subRubroRepositorio.findById(id);
            
            modelo.put("subRubro", subRubroOptional.get());
            modelo.addAttribute("rubros", rubros);
            redirectAttributes.addFlashAttribute("exito", "Subrubro actualizado exitosamente");
            
            subRubroServicio.modificarSubRubro(nombre, id, idRubro);
            
            return "redirect:../lista";
        } catch (MiException ex) {
            
            List<Rubro> rubros = rubroServicio.listarRubros();
            Optional<SubRubro> subRubroOptional = subRubroRepositorio.findById(id);
                        
            modelo.put("subRubro", subRubroOptional.get());
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("rubros", rubros);
            modelo.addAttribute("id", id);
            modelo.addAttribute("nombre", nombre);
            return "subRubro_modificar.html";
        }
    }
    
}
