package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.Movimiento;
import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Repositorios.ProductoRepositorio;
import com.egg.almacen.Servicios.MovimientoServicio;
import com.egg.almacen.Servicios.ProductoServicio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movimiento")
public class MovimientoControlador {

    @Autowired
    private MovimientoServicio movimientoServicio;
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ProductoRepositorio productoRepositorio;
        

@GetMapping("/lista")
public String listarMovimientos(Model model) {
    // Obtener todos los movimientos
    List<Movimiento> movimientos = movimientoServicio.listarMovimiento();

    // Calcular existencias cronológicamente
    movimientos = movimientoServicio.calcularExistencias(movimientos);

    // Extraer valores únicos para los filtros
    Set<String> proveedoresClientes = movimientos.stream()
        .map(Movimiento::getProvCli)
        .collect(Collectors.toSet());
    Set<String> productosNombres = movimientos.stream()
        .map(Movimiento::getProducto)
        .collect(Collectors.toSet());
    Set<String> tipos = movimientos.stream()
        .map(Movimiento::getTipo)
        .collect(Collectors.toSet());

    // Agregar datos al modelo
    model.addAttribute("movimientos", movimientos);
    model.addAttribute("proveedoresClientes", proveedoresClientes);
    model.addAttribute("productosNombres", productosNombres);
    model.addAttribute("tipos", tipos);

    return "movimiento_list"; // Vista de Thymeleaf
}

}
