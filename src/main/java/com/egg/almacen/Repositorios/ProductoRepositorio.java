
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Entidades.SubRubro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String>{
    
Producto findByCodigo(String codigo);
    
boolean existsByRubroId(String rubroId);
boolean existsBySubRubroId(String subRubroId);

@Query("SELECT l FROM Producto l WHERE l.descripcion = :descripcion")
public Producto buscarPorDescripcion(@Param("descripcion")String descripcion);

    @Query("SELECT p.descripcion, p.existencia FROM Producto p")
    List<Object[]> obtenerExistenciasActuales();


long countByRubro(Rubro rubro);
long countBySubRubro(SubRubro subRubro);


    
}


