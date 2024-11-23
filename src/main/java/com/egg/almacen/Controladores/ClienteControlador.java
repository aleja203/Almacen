
package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.CuentaCorriente;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Repositorios.ClienteRepositorio;
import com.egg.almacen.Servicios.ClienteServicio;
import com.egg.almacen.Servicios.CuentaCorrienteServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private CuentaCorrienteServicio cuentaCorrienteServicio;
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ClienteServicio clienteServicio;
    
    @GetMapping("/registrar")
    public String registrar(){
        return "cliente_form.html";
    }
    
        @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long dni, 
                           @RequestParam String nombre,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String domicilio,
                           @RequestParam(required = false) Long telefono,
                           ModelMap modelo) {

        try {

            clienteServicio.crearCliente(dni, nombre, email, domicilio, telefono);

            modelo.put("exito", "El cliente fue cargado correctamente!");

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "cliente_form.html";
        }
        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        
        List <Cliente> clientes = clienteServicio.listarClientes();
        
        modelo.addAttribute("clientes", clientes);
        
        return "cliente_list.html";
        
    }
    
     @GetMapping("/eliminar/{dni}")
    public String eliminarProducto(@PathVariable Long dni, RedirectAttributes redirectAttributes) {
        try {
            clienteServicio.eliminarCliente(dni);
            redirectAttributes.addFlashAttribute("exito", "El cliente ha sido eliminado exitosamente.");
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/lista";
    }
    
    @GetMapping("/modificar/{dni}")
    public String modificar(@PathVariable Long dni, ModelMap modelo){
        modelo.put("cliente", clienteServicio.getOne(dni));
        
        return "cliente_modificar.html";
    }
    
    @PostMapping("/modificar/{dni}")
    public String modificar(@PathVariable Long dni,
                            @RequestParam String nombre,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String domicilio,
                            @RequestParam(required = false) Long telefono,
                            ModelMap modelo,
                            RedirectAttributes redirectAttributes) {
        try {
            
            Optional<Cliente> clienteOptional = clienteRepositorio.findById(dni);
            
            modelo.put("producto", clienteOptional.get());
            redirectAttributes.addFlashAttribute("exito", "Cliente actualizado exitosamente");
            
            clienteServicio.modificarCliente(dni, nombre, email, domicilio, telefono);
            
            return "redirect:../lista";
        } catch (MiException ex) {
            
            Optional<Cliente> clienteOptional = clienteRepositorio.findById(dni);
                        
            modelo.put("cliente", clienteOptional.get());
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("id", dni);
            modelo.addAttribute("nombre", nombre);
            return "cliente_modificar.html";
        }
    }
    
//    @GetMapping("/listarCtaCte/{dni}")
//    public String listarCuentaCorriente(@PathVariable Long dni, Model model) {
//        // Obtener todos los movimientos
//        List<CuentaCorriente> cuentasCorrientes = cuentaCorrienteServicio.listarCuentasCorrientesPorDni(dni);
//
//        // Obtener el nombre del cliente y el saldo, si existen movimientos
//        String nombreCliente = "Sin cliente asignado";
//        Double saldoCliente = 0.0;
//
//        if (cuentasCorrientes != null && !cuentasCorrientes.isEmpty() && cuentasCorrientes.get(0).getCliente() != null) {
//            Cliente cliente = cuentasCorrientes.get(0).getCliente();
//            nombreCliente = cliente.getNombre();
//            saldoCliente = cliente.getSaldo(); // Suponiendo que `Cliente` tiene un método `getSaldo()`
//        }
//
//        // Agregar los datos al modelo para ser usados en el frontend
//        model.addAttribute("cuentasCorrientes", cuentasCorrientes);
//        model.addAttribute("nombreCliente", nombreCliente);
//        model.addAttribute("saldoCliente", saldoCliente);
//
//        return "cuentaCorriente_list";  // La vista de Thymeleaf para mostrar la lista de movimientos
//    }

@GetMapping("/listarCtaCte/{dni}")
public String listarCuentaCorriente(@PathVariable Long dni, Model model) {
    
     List<CuentaCorriente> cuentasCorrientes = cuentaCorrienteServicio.listarCuentaCorriente(dni);
    

    // Calcular los saldos
    cuentasCorrientes = cuentaCorrienteServicio.calcularSaldos(cuentasCorrientes);

    String nombreCliente = "Sin cliente asignado";
    Double saldoCliente = 0.0;

    if (cuentasCorrientes != null && !cuentasCorrientes.isEmpty() && cuentasCorrientes.get(0).getCliente() != null) {
        Cliente cliente = cuentasCorrientes.get(0).getCliente();
        nombreCliente = cliente.getNombre();
        saldoCliente = cliente.getSaldo(); // Obtener saldo inicial del cliente
    }

    // Agregar los datos al modelo para ser usados en el frontend
    model.addAttribute("cuentasCorrientes", cuentasCorrientes);
    model.addAttribute("nombreCliente", nombreCliente);
    model.addAttribute("saldoCliente", saldoCliente);

    return "cuentaCorriente_list"; // La vista de Thymeleaf para mostrar la lista de movimientos
}













}
