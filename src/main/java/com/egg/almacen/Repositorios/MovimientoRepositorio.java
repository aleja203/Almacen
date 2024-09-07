
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, String>{
    
}
