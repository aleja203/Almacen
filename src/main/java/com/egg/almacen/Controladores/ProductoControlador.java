
package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Entidades.SubRubro;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Repositorios.RubroRepositorio;
import com.egg.almacen.Repositorios.SubRubroRepositorio;
import com.egg.almacen.Servicios.ProductoServicio;
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
@RequestMapping("/producto")
public class ProductoControlador {
    
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private RubroServicio rubroServicio;    
    @Autowired
    private RubroRepositorio rubroRepositorio;
    @Autowired
    private SubRubroRepositorio subRubroRepositorio;
    @Autowired
    private SubRubroServicio subRubroServicio;
    
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Rubro> rubros = rubroServicio.listarRubros();
        modelo.addAttribute("rubros", rubros);
        
        List<SubRubro> subRubros = subRubroServicio.listarSubRubros();
        modelo.addAttribute("subRubros", subRubros);
        
        return "producto_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) String codigo, 
                           @RequestParam String descripcion,
                           @RequestParam(required = false) String idRubro,
                           @RequestParam(required = false) String idSubRubro,
                           @RequestParam(required = false) Double costo,
                           @RequestParam(required = false) Double precioDeVenta,
                           ModelMap modelo) {

        try {

            productoServicio.crearProducto(codigo, descripcion, idRubro, idSubRubro, costo, precioDeVenta);

            modelo.put("exito", "El producto fue cargado correctamente!");

        } catch (MiException ex) {
            List<Rubro> rubros = rubroServicio.listarRubros();
            List<SubRubro> subRubros = subRubroServicio.listarSubRubros();

            modelo.addAttribute("rubros", rubros);
            modelo.addAttribute("subRubros", subRubros);
            modelo.put("error", ex.getMessage());
            return "producto_form.html";
        }
        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        
        List<Rubro> rubros = rubroServicio.listarRubros();
        modelo.addAttribute("rubros", rubros);
        
        List<SubRubro> subRubros = subRubroServicio.listarSubRubros();
        modelo.addAttribute("subRubros", subRubros);
        
        List<Producto> productos = productoServicio.listarProductos();
        modelo.addAttribute("productos", productos);

        return "producto_list";
    }
    
    @GetMapping("/modificar/{codigo}")
    public String modificar(@PathVariable String codigo, ModelMap modelo){
        modelo.put("producto", productoServicio.getOne(codigo));
        
        List<Rubro> rubros = rubroServicio.listarRubros();
        modelo.addAttribute("rubros", rubros);
        
        List<SubRubro> subRubros = subRubroServicio.listarSubRubros();
        modelo.addAttribute("subRubros", subRubros);
        
        return "producto_modificar.html";
    }
    
    @PostMapping("/modificar/{codigo}")
    public String modificar(@PathVariable String codigo,
                            @RequestParam String descripcion,
                            @RequestParam String idRubro,
                            @RequestParam String idSubRubro,
                            @RequestParam Double costo,
                            @RequestParam Double precioDeVenta,
                            ModelMap modelo,
                            RedirectAttributes redirectAttributes) {
        try {
            
            List<Rubro> rubros = rubroServicio.listarRubros();
            List<SubRubro> subRubros = subRubroServicio.listarSubRubros();
            System.out.println("idRubro recibido: " + idRubro);
            System.out.println("idSubRubro recibido: " + idSubRubro);           
            Optional<Producto> productoOptional = productoRepositorio.findById(codigo);
            
            modelo.put("producto", productoOptional.get());
            modelo.addAttribute("rubros", rubros);
            modelo.addAttribute("subRubros", subRubros);
            redirectAttributes.addFlashAttribute("exito", "Subrubro actualizado exitosamente");
            
            productoServicio.modificarProducto(codigo, descripcion, idRubro, idSubRubro, costo, precioDeVenta);
            
            return "redirect:../lista";
        } catch (MiException ex) {
            
            List<Rubro> rubros = rubroServicio.listarRubros();
            List<SubRubro> subRubros = subRubroServicio.listarSubRubros();
            Optional<Producto> productoOptional = productoRepositorio.findById(codigo);
                        
            modelo.put("producto", productoOptional.get());
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("rubros", rubros);
            modelo.addAttribute("id", codigo);
            modelo.addAttribute("nombre", descripcion);
            return "producto_modificar.html";
        }
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminarProducto(@PathVariable String codigo, RedirectAttributes redirectAttributes) {
        try {
            productoServicio.eliminarProducto(codigo);
            redirectAttributes.addFlashAttribute("exito", "El producto ha sido eliminado exitosamente.");
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/producto/lista";
    }
    
}
