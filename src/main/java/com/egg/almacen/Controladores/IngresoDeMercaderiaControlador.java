
package com.egg.almacen.Controladores;

import com.egg.almacen.DTO.IngresoDeMercaderiaDTO;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Servicios.IngresoDeMercaderiaServicio;
import com.egg.almacen.Servicios.ProductoServicio;
import com.egg.almacen.Servicios.ProveedorServicio;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/IngresoDeMercaderia")
public class IngresoDeMercaderiaControlador {
    
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private IngresoDeMercaderiaServicio ingresoDeMercaderiaServicio;
    
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("proveedores", proveedores);
        
                
        return "venta_form.html";
    }
    
    @PostMapping("/registro")
    //@Transactional
    public ResponseEntity<Map<String, Object>> registrarIngresoDeMercaderia(@RequestBody IngresoDeMercaderiaDTO ingresoDeMercaderiaDTO) {
        // Llamamos al servicio que ahora maneja tanto la creación de la venta como las excepciones
        System.out.println("Contenido del JSON: " + ingresoDeMercaderiaDTO);

        System.out.println("Proveedor: " + ingresoDeMercaderiaDTO.getProveedorId());
        
        
        
        
        
        Map<String, Object> response = ingresoDeMercaderiaServicio.crearIngresoDeMercaderia(ingresoDeMercaderiaDTO);

        // Si el servicio devolvió un mensaje de error, lo respondemos con HTTP 500
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Si todo fue exitoso, devolvemos una respuesta con HTTP 200
        return ResponseEntity.ok(response);
    }

    
    
}
