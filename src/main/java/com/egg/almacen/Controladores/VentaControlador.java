package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Servicios.ProductoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/venta")
public class VentaControlador {
    
    @Autowired
    private ProductoServicio productoServicio;
   
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();
        modelo.addAttribute("productos", productos);
        
                
        return "venta_form.html";
    }
    
}
