
package com.egg.almacen.Controladores;

import com.egg.almacen.DTO.CobroDTO;
import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.FormaDePago;
import com.egg.almacen.Enumeraciones.TipoFormaPago;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import com.egg.almacen.Servicios.ClienteServicio;
import com.egg.almacen.Servicios.CobroServicio;
import com.egg.almacen.Servicios.FormaDePagoServicio;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cobro")
public class CobroControlador {
    
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private FormaDePagoServicio formaDePagoServicio;
    @Autowired
    private CobroServicio cobroServicio;    
    
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        
        List<Cliente> clientes = clienteServicio.listarClientes();
        List<FormaDePago> formasDePago = formaDePagoServicio.listarFormasDePago();
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("tiposFormaPago", TipoFormaPago.values());
        modelo.addAttribute("formasDePago", formasDePago);
        
                
        return "cobro_form.html";
    }
    
    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registrarCobro(@RequestBody CobroDTO cobroDTO) {

        Map<String, Object> response = cobroServicio.crearCobro(cobroDTO);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/ingresar/{dni}")
    public String ingresar(@PathVariable Long dni, ModelMap modelo){
        
        Optional<Cliente> clienteOpt = clienteRepositorio.findById(dni);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();  // Obtener el objeto Cliente
            modelo.addAttribute("cliente", cliente);
        } else {
            // Si el cliente no se encuentra, podemos manejarlo como lo consideres apropiado
            modelo.addAttribute("cliente", null);  // O enviar un mensaje de error
        }
        
        
        List<FormaDePago> formasDePago = formaDePagoServicio.listarFormasDePago();
        //modelo.addAttribute("cliente", cliente);
        modelo.addAttribute("tiposFormaPago", TipoFormaPago.values());
        modelo.addAttribute("formasDePago", formasDePago);
        
                
        return "cobroDni_form.html";
    }
    
    @PostMapping("/ingreso/{dni}")
    public ResponseEntity<Map<String, Object>> ingresarCobro(@RequestBody CobroDTO cobroDTO) {
        System.out.println("Recibiendo solicitud con CobroDTO: " + cobroDTO);
        // Accedemos al dni del cliente dentro del CobroDTO
    Long dniCliente = cobroDTO.getClienteId();

    // Imprimimos el dni (esto es solo un ejemplo, puedes hacer lo que necesites con él)
    System.out.println("DNI del cliente: " + dniCliente);

        Map<String, Object> response = cobroServicio.crearCobro(cobroDTO);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }
    
}
