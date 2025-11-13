package com.ega.cms.modelo;

// Descripción: Excepción personalizada para la programación defensiva
// Se lanza cuando un usuario intenta realizar una acción sin los permisos adecuados

public class PermisoException extends Exception {
    public PermisoException(String mensaje) {
        super(mensaje);
    }
}