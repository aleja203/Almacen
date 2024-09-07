
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.IngresoDeMercaderia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngresoDeMercaderiaRepositorio extends JpaRepository<IngresoDeMercaderia, String>{
    
}
