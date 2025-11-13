package com.ega.cms.modelo;

// Usuario.java
// Descripción: Representa a un usuario del CMS.

public class Usuario
{
    private int id;
    private String username;
    private RolUsuario rol;

    public Usuario(int id, String username, RolUsuario rol)
    {
        this.id = id;
        this.username = username;
        this.rol = rol;
    }

    // --- Getters ---
    public String getUsername() { return username; }
    public RolUsuario getRol() { return rol; }

    // Simulación de un método de login
    public boolean login(String pass) 
    {
        return true; 
    }

    @Override
    public String toString()
    {
        return username + " (" + rol + ")";
    }
}