package com.ega.cms.modelo;


// Subclase de Contenido. Implementa Publicable y la categoría Informativo.

public class Imagen extends Contenido implements Publicable, Informativo {
    private String url;        
    private String resolucion; 

    public Imagen(int id, String titulo, Usuario autor, String categoria, String url, String resolucion) {
        super(id, titulo, autor, categoria);
        this.url = url;
        this.resolucion = resolucion;
    }

    @Override
    public String getTipo() { return "Imagen"; }

    @Override
    public String mostrar() {
        return "--- MOSTRANDO IMAGEN ---\n" + getTitulo() + "\nResolución: " + resolucion + "\nURL: " + url;
    }

    @Override
    public String publicar() {
        this.setEstado("Publicado"); // se actualiza el estado
        return "Imagen '" + getTitulo() + "' publicada en el banner informativo.";
    }
}