
package com.egg.almacen.Controladores;

import com.egg.almacen.Entidades.FormaDePago;
import com.egg.almacen.Enumeraciones.TipoFormaPago;
import com.egg.almacen.Excepciones.MiException;
import com.egg.almacen.Servicios.FormaDePagoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/formaDePago")
public class FormaDePagoControlador {
    
    @Autowired
    private FormaDePagoServicio formaDePagoServicio;
    
    @GetMapping("/registrar")
public String registrar(ModelMap modelo) {
    modelo.addAttribute("tiposFormaPago", TipoFormaPago.values());
    return "formaDePago_form.html";
}

@PostMapping("/registro")
public String registro(@RequestParam(required = false) String descripcion, 
                       @RequestParam(required = false) String idTipoFormaPago,  
                       ModelMap modelo) {

    try {
        // Verificación y conversión del tipo de forma de pago
        if (idTipoFormaPago == null || idTipoFormaPago.isEmpty()) {
            throw new MiException("Debe seleccionar un tipo de forma de pago.");
        }

        // Intentar convertir idTipoFormaPago al enum TipoFormaPago
        TipoFormaPago tipoFormaPago;
        try {
            tipoFormaPago = TipoFormaPago.valueOf(idTipoFormaPago);
        } catch (IllegalArgumentException e) {
            throw new MiException("Tipo de forma de pago inválido.");
        }

        // Llamar al servicio pasando el tipo ya convertido
        formaDePagoServicio.crearFormaDePago(descripcion, tipoFormaPago);

        modelo.put("exito", "La forma de pago fue cargada correctamente!");

    } catch (MiException ex) {
        // Manejo de error: volver a cargar la lista de formas de pago
        List<FormaDePago> formasDePago = formaDePagoServicio.listarFormasDePago();
        modelo.addAttribute("rubros", formasDePago);

        modelo.put("error", ex.getMessage());
        return "formaDePago_form.html";
    }
    return "index.html";
}

    
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<FormaDePago> formasDePago = formaDePagoServicio.listarFormasDePago();
        formasDePago.forEach(forma -> System.out.println("Tipo: " + forma.getTipo()));

        modelo.addAttribute("formasDePago", formasDePago);
        modelo.addAttribute("tiposFormaPago", TipoFormaPago.values());
        

        return "formaDePago_list";
    }
    
}
