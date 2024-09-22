
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.DetalleIngreso;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DetalleIngresoRepositorio extends JpaRepository<DetalleIngreso, Long>{
    
}
