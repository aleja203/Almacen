
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, String>{
    
}
