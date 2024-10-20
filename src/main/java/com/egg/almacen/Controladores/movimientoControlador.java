package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Movimiento;
import com.egg.almacen.Servicios.MovimientoServicio;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movimiento")
public class movimientoControlador {

    @Autowired
    private MovimientoServicio movimientoServicio;
        
    @GetMapping("/lista")
    public String listarMovimientos(Model model) {
        // Obtener todos los movimientos
        List<Movimiento> movimientos = movimientoServicio.listarMovimiento();
        
        // Extraer los valores únicos de Proveedor/Cliente, Tipo y Producto
        Set<String> proveedoresClientes = movimientos.stream()
            .map(Movimiento::getProvCli)
            .collect(Collectors.toSet());

        Set<String> productos = movimientos.stream()
            .map(Movimiento::getProducto)
            .collect(Collectors.toSet());

        Set<String> tipos = movimientos.stream()
            .map(Movimiento::getTipo)
            .collect(Collectors.toSet());

        // Agregar los datos al modelo para ser usados en el frontend
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("proveedoresClientes", proveedoresClientes); // Lista de proveedores/clientes (String)
        model.addAttribute("productos", productos); // Lista de productos (String)
        model.addAttribute("tipos", tipos); // Lista de tipos (VENTA, COMPRA) (String)

        return "movimiento_list";  // La vista de Thymeleaf para mostrar la lista de movimientos
    }

    
}
