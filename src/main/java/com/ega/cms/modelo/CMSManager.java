package com.ega.cms.modelo;

import java.util.ArrayList;

// Clase principal del Modelo. Gestiona toda la lógica de negocio

public class CMSManager {
    private ArrayList<Contenido> listaContenidos;
    private ArrayList<Usuario> listaUsuarios;    
    private Usuario usuarioActual;               
    private int proximoIdContenido = 3; // Inicia después de los de ejemplo

    public CMSManager() {
        listaContenidos = new ArrayList<>();
        listaUsuarios = new ArrayList<>();
        // Cargar usuarios iniciales
        listaUsuarios.add(new Usuario(1, "S. Garcia (Admin)", RolUsuario.ADMINISTRADOR));
        listaUsuarios.add(new Usuario(2, "C. Chávez (Editor)", RolUsuario.EDITOR));
    }

    public boolean login(String user, String pass) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equals(user)) {
                if (u.login(pass)) { // Simula la validación
                    this.usuarioActual = u;
                    return true;
                }
            }
        }
        return false;
    }

    public Usuario getUsuarioActual() { return usuarioActual; }


    // Lógica para crear contenido, validando permisos
    public void crearContenido(Contenido c) throws PermisoException { 
        if (usuarioActual == null)
            throw new PermisoException("Debe iniciar sesión para crear contenido.");

        if (usuarioActual.getRol() == RolUsuario.EDITOR || usuarioActual.getRol() == RolUsuario.ADMINISTRADOR) {
            listaContenidos.add(c);
        } else {
            throw new PermisoException("No tiene permisos para crear contenido.");
        }
    }

    
    // Lógica para eliminar contenido
    // Solo Admins pueden eliminar
    
    public void eliminarContenido(Contenido c) throws PermisoException {
        if (usuarioActual == null)
            throw new PermisoException("Debe iniciar sesión.");
            
        if (usuarioActual.getRol() == RolUsuario.ADMINISTRADOR) {
            listaContenidos.remove(c);
        } else {
            throw new PermisoException("Solo los Administradores pueden eliminar contenido.");
        }
    }
    
    
    // Lógica para publicar contenido
    // Solo Admins pueden publicar
    public String publicarContenido(Publicable c) throws PermisoException { 
        if (usuarioActual == null)
            throw new PermisoException("Debe iniciar sesión.");

        if (usuarioActual.getRol() == RolUsuario.ADMINISTRADOR) {
            return c.publicar(); // Llama al método polimórfico
        } else {
            throw new PermisoException("Solo los Administradores pueden publicar contenido.");
        }
    }

    public ArrayList<Contenido> getContenidosParaVista() {
        return new ArrayList<>(listaContenidos); // Devuelve una copia
    }

    // Filtra la lista de contenidos usando las interfaces de categoría
    public ArrayList<Contenido> filtrarPorCategoria(String categoria) {
        ArrayList<Contenido> filtrados = new ArrayList<>();
        for (Contenido c : listaContenidos) {
            // Compara el string de categoría directamente
            if (c.getCategoria().equalsIgnoreCase(categoria)) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }
    
    public ArrayList<String> generarReporte() { 
        ArrayList<String> reporte = new ArrayList<>();
        reporte.add("--- Reporte General del CMS ---");
        reporte.add("Total de Contenidos: " + listaContenidos.size());
        reporte.add("Total de Usuarios: " + listaUsuarios.size());
        return reporte;
    }

    // Método auxiliar para cargar datos de ejemplo como en el boceto
    public void cargarDatosIniciales() {
        if (usuarioActual == null) {
            // Simula un login de admin si nadie ha iniciado sesión
            // para asegurar que la carga inicial funciones
            login("S. Garcia (Admin)", "");
        }
        
        Usuario admin = listaUsuarios.get(0);
        Usuario editor = listaUsuarios.get(1);

        try { 
            Video v = new Video(1, "Tour EGA", editor, "Promocional", "https://ega.uvg.edu.gt/tour", 180);
            v.addEtiqueta("EGA");
            v.addEtiqueta("Estudio UVG");
            crearContenido(v);

            Articulo a = new Articulo(2, "Guía de estudio", editor, "Tutorial", "Este es el texto...");
            a.addEtiqueta("Guía");
            crearContenido(a);

        } catch (PermisoException e) {
            e.printStackTrace();
        }
    }
}