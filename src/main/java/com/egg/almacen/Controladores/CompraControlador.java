
package com.egg.almacen.Controladores;

import com.egg.almacen.DTO.CompraDTO;
import com.egg.almacen.Entidades.Compra;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Proveedor;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Servicios.CompraServicio;
import com.egg.almacen.Servicios.ProductoServicio;
import com.egg.almacen.Servicios.ProveedorServicio;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/compra")
public class CompraControlador {
    
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private CompraServicio compraServicio;
    
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("proveedores", proveedores);
        
                
        return "compra_form.html";
    }
    
    @PostMapping("/registro")
    //@Transactional
    public ResponseEntity<Map<String, Object>> registrarCompra(@RequestBody CompraDTO compraDTO) {
        // Llamamos al servicio que ahora maneja tanto la creación de la venta como las excepciones
        System.out.println("Contenido del JSON: " + compraDTO);

        System.out.println("Proveedor: " + compraDTO.getProveedorId());
        
        Map<String, Object> response = compraServicio.crearCompra(compraDTO);

        // Si el servicio devolvió un mensaje de error, lo respondemos con HTTP 500
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Si todo fue exitoso, devolvemos una respuesta con HTTP 200
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listaDetalle")
    public String listarComprasDetalle(Model model) {
        List<Map<String, Object>> compras = compraServicio.listarDetalle();
        List<Producto> productos = productoServicio.listarProductos();
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();

        model.addAttribute("compras", compras);
        model.addAttribute("productos", productos);
        model.addAttribute("proveedores", proveedores);

        return "compraDetalle_list";
    }
 
    @GetMapping("/listaCompra")
    public String listarCompras(Model model) {
        List<Compra> compras = compraServicio.listarCompra();

        List<Proveedor> proveedores = proveedorServicio.listarProveedores();

        model.addAttribute("compras", compras);
        model.addAttribute("proveedores", proveedores);
        return "compra_list";
    }
    
     @GetMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            compraServicio.eliminarCompra(id);
            redirectAttributes.addFlashAttribute("exito", "La compra ha sido eliminada exitosamente.");
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/venta/listaVenta";
    }
}
