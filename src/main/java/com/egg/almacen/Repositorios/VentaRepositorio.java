
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Venta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long>{
    
    @Query("SELECT v FROM Venta v JOIN FETCH v.detalles d JOIN FETCH v.cliente c")
    List<Venta> listarConDetalles();
    
    
    @Query("SELECT v FROM Venta v JOIN FETCH v.formasPago f JOIN FETCH v.cliente c")
    List<Venta> listarConFormasPago();
    
}
