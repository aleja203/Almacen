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
    private VentaServicio ventaServicio;
   
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();
        modelo.addAttribute("productos", productos);
        
                
        return "venta_form.html";
    }
    
//    @PostMapping("/registro")
//    @Transactional
//    public ResponseEntity<Map<String, String>> registrarVenta(@RequestBody VentaDTO ventaDTO) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            // Convertir DTO a entidad y guardar
//            Venta venta = ventaServicio.crearVenta(ventaDTO);
//
//            // Configurar respuesta exitosa
//            response.put("message", "Venta registrada exitosamente");
//            return ResponseEntity.ok(response);
//        } catch (MiException e) {
//            // Manejo de errores específico para MiException
//            System.out.println("MiException1 capturada: " + e.getMessage());
//            response.put("error", "Datos incompletos o inválidos para la venta.");
//            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            return ResponseEntity.ok(response);
//        } catch (UnexpectedRollbackException e) {
//            // Manejo específico de la excepción de rollback
//            System.out.println("MiException2 capturada: " + e.getMessage());
//            response.put("error", "La operación fue revertida debido a un error en la transacción.");
//            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // HTTP 500 (Internal Server Error)
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            // Manejo de errores
//            System.out.println("MiException3 capturada: " + e.getMessage());
//            response.put("error", "Error al registrar la venta: " + "Datos incompletos o inválidos para la venta.");
//            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//            return ResponseEntity.ok(response);
//        }
//    }

    @PostMapping("/registro")
    @Transactional
    public ResponseEntity<Map<String, Object>> registrarVenta(@RequestBody VentaDTO ventaDTO) {
        // Llamamos al servicio que ahora maneja tanto la creación de la venta como las excepciones
        Map<String, Object> response = ventaServicio.crearVenta(ventaDTO);

        // Si el servicio devolvió un mensaje de error, lo respondemos con HTTP 500
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Si todo fue exitoso, devolvemos una respuesta con HTTP 200
        return ResponseEntity.ok(response);
    }

    
}
