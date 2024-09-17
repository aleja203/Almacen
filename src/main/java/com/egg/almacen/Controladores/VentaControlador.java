package com.egg.almacen.Controladores;


import com.egg.almacen.DTO.VentaDTO;
import com.egg.almacen.Entidades.DetalleVenta;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Servicios.ProductoServicio;
import com.egg.almacen.Servicios.VentaServicio;
import java.util.HashMap;
//import com.egg.almacen.Servicios.VentaServicio;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/venta")
public class VentaControlador {
    
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private VentaServicio ventaServicio;
   
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();
        modelo.addAttribute("productos", productos);
        
                
        return "venta_form.html";
    }
    
    @PostMapping("/registro")
    @Transactional
public ResponseEntity<Map<String, String>> registrarVenta(@RequestBody VentaDTO ventaDTO) {
    Map<String, String> response = new HashMap<>();
    try {
        // Convertir DTO a entidad y guardar
        Venta venta = ventaServicio.crearVenta(ventaDTO);
        
        // Configurar respuesta exitosa
        response.put("message", "Venta registrada exitosamente");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        // Manejo de errores
        response.put("error", "Error al registrar la venta: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

}
