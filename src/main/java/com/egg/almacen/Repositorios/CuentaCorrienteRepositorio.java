
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.CuentaCorriente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CuentaCorrienteRepositorio extends JpaRepository<CuentaCorriente, Long>{
    
    List<CuentaCorriente> findByClienteDni(Long dni);
    
}
