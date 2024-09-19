package com.egg.almacen.Excepciones;

import org.springframework.dao.DataIntegrityViolationException;


public class MiException extends Exception{

    public MiException(String msg) {
        super(msg);
    }

    public MiException(String datos_incompletos_o_inválidos_para_la_ven, DataIntegrityViolationException e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
