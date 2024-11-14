
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Cobro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobroRepositorio extends JpaRepository<Cobro, Long>{
    
}
