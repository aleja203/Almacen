package com.egg.almacen.Controladores;


import com.egg.almacen.DTO.VentaDTO;
import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.DetalleVenta;
import com.egg.almacen.Entidades.FormaDePago;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Venta;
import com.egg.almacen.Enumeraciones.TipoFormaPago;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Servicios.ClienteServicio;
import com.egg.almacen.Servicios.FormaDePagoServicio;
import com.egg.almacen.Servicios.ProductoServicio;
import com.egg.almacen.Servicios.VentaServicio;
import java.util.HashMap;
//import com.egg.almacen.Servicios.VentaServicio;
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
@RequestMapping("/venta")
public class VentaControlador {
    
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private VentaServicio ventaServicio;
    @Autowired
    private FormaDePagoServicio formaDePagoServicio;
   
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();
        List<Cliente> clientes = clienteServicio.listarClientes();
        List<FormaDePago> formasDePago = formaDePagoServicio.listarFormasDePago();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("tiposFormaPago", TipoFormaPago.values());
        modelo.addAttribute("formasDePago", formasDePago);
        
                
        return "venta_form.html";
    }
    
    @PostMapping("/registro")
    //@Transactional
    public ResponseEntity<Map<String, Object>> registrarVenta(@RequestBody VentaDTO ventaDTO) {

        Map<String, Object> response = ventaServicio.crearVenta(ventaDTO);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listaDetalle")
    public String listarVentasDetalle(Model model) {
        List<Map<String, Object>> ventas = ventaServicio.listarDetalle();
        List<Producto> productos = productoServicio.listarProductos();
        List<Cliente> clientes = clienteServicio.listarClientes();

        model.addAttribute("ventas", ventas);
        model.addAttribute("productos", productos);
        model.addAttribute("clientes", clientes);

        return "ventaDetalle_list";
    }
    
    @GetMapping("/listaVenta")
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaServicio.listarVenta();

        List<Cliente> clientes = clienteServicio.listarClientes();

        model.addAttribute("ventas", ventas);
        model.addAttribute("clientes", clientes);
        return "venta_list";
    }
    
     @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ventaServicio.eliminarVenta(id);
            redirectAttributes.addFlashAttribute("exito", "La venta ha sido eliminada exitosamente.");
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/venta/listaVenta";
    }
}
