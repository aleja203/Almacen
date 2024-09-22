package com.egg.almacen.Controladores;


import com.egg.almacen.DTO.VentaDTO;
import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.DetalleVenta;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Servicios.ClienteServicio;
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
import org.springframework.transaction.UnexpectedRollbackException;
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
    private ClienteServicio clienteServicio;
    @Autowired
    private VentaServicio ventaServicio;
   
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();
        List<Cliente> clientes = clienteServicio.listarClientes();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("clientes", clientes);
        
                
        return "venta_form.html";
    }
    
    @PostMapping("/registro")
    //@Transactional
    public ResponseEntity<Map<String, Object>> registrarVenta(@RequestBody VentaDTO ventaDTO) {
        // Llamamos al servicio que ahora maneja tanto la creación de la venta como las excepciones
        System.out.println("Contenido del JSON: " + ventaDTO);

        System.out.println("Cliente: " + ventaDTO.getClienteId());
        
        
        
        
        
        Map<String, Object> response = ventaServicio.crearVenta(ventaDTO);

        // Si el servicio devolvió un mensaje de error, lo respondemos con HTTP 500
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Si todo fue exitoso, devolvemos una respuesta con HTTP 200
        return ResponseEntity.ok(response);
    }

    
}
