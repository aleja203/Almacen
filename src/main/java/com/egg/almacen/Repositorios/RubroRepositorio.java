
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Rubro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RubroRepositorio extends JpaRepository<Rubro, String>{

}
