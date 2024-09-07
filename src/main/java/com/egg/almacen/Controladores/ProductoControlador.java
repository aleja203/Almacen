
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            return "subRubro_form.html";
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
    
}
