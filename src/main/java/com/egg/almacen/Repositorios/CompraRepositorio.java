
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Compra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepositorio extends JpaRepository<Compra, Long>{
    
    @Query("SELECT c FROM Compra c JOIN FETCH c.detalles d JOIN FETCH c.proveedor p ORDER BY c.fecha DESC")
    List<Compra> listarConDetalles();
    
}
