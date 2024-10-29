
package com.egg.almacen.Enumeraciones;


public enum TipoFormaPago {
    DEBITO("DEBITO"),
    CREDITO("CREDITO"),
    TRANSFERENCIA("TRANSFERENCIA"),
    CONTADO("CONTADO"),
    CUENTA_CORRIENTE("CUENTA_CORRIENTE");

    private final String displayName;

    // Constructor para asignar el valor personalizado a cada constante
    TipoFormaPago(String displayName) {
        this.displayName = displayName;
    }

    // Método toString para devolver el nombre personalizado
    @Override
    public String toString() {
        return displayName;
    }
}
