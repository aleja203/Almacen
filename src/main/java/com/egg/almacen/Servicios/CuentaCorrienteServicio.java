
package com.egg.almacen.Servicios;

import com.egg.almacen.Entidades.Cliente;
import com.egg.almacen.Entidades.CuentaCorriente;
import com.egg.almacen.Repositorios.CuentaCorrienteRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaCorrienteServicio {
    
    @Autowired
    private CuentaCorrienteRepositorio cuentaCorrienteRepositorio;
    
    public List<CuentaCorriente> listarCuentaCorriente(Long dni) {
        return cuentaCorrienteRepositorio.findByCliente_DniOrderByIdDesc(dni);

    }

public List<CuentaCorriente> calcularSaldos(List<CuentaCorriente> cuentasCorrientes) {
    // Variable para rastrear el saldo acumulativo
    double saldoAcumulado = 0.0;

    // Iterar en orden ascendente (de más antiguo a más reciente)
    for (int i = cuentasCorrientes.size() - 1; i >= 0; i--) {
        CuentaCorriente cuentaCorriente = cuentasCorrientes.get(i);

        // Ajustar el saldo según el tipo de movimiento
        if ("VENTA".equalsIgnoreCase(cuentaCorriente.getTipo())) {
            saldoAcumulado += cuentaCorriente.getImporte(); // Restar en caso de venta
        } else if ("COBRO".equalsIgnoreCase(cuentaCorriente.getTipo())) {
            saldoAcumulado += cuentaCorriente.getImporte(); // Sumar en caso de cobro
        }

        // Actualizar el saldo acumulado en el movimiento
        cuentaCorriente.setSaldoRestante(saldoAcumulado);
    }

    return cuentasCorrientes;
}
}
