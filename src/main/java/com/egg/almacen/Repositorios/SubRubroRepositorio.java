
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.SubRubro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubRubroRepositorio extends JpaRepository<SubRubro, String>{
    
}
