
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Producto;
import com.egg.almacen.Entidades.Rubro;
import com.egg.almacen.Entidades.SubRubro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String>{
    
boolean existsByRubroId(String rubroId);

@Query("SELECT l FROM Producto l WHERE l.descripcion = :descripcion")
public Producto buscarPorDescripcion(@Param("descripcion")String descripcion);

long countByRubro(Rubro rubro);
long countBySubRubro(SubRubro subRubro);
    
}
