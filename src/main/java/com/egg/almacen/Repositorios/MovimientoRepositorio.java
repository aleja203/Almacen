
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Movimiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long>{
    List<Movimiento> findByFactura(Long facturaId);
    List<Movimiento> findByFacturaOrderByFechaAsc(Long facturaId);
    
    @Query("SELECT m FROM Movimiento m ORDER BY m.id DESC")
    List<Movimiento> findAllOrderedByIdDesc();
    
}
