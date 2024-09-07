
package com.egg.almacen.Repositorios;

import com.egg.almacen.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, String>{
    
}
