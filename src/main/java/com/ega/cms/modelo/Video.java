package com.ega.cms.modelo;


// Subclase de Contenido. Implementa Publicable y la categoría Estudiantil.
public class Video extends Contenido implements Publicable, Estudiantil { 
    private int duracion; 
    private String url;

    public Video(int id, String titulo, Usuario autor, String categoria, String url, int duracion) { 
        super(id, titulo, autor, categoria);
        this.url = url;
        this.duracion = duracion;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public String getTipo() { return "Video"; }

    @Override
    public String mostrar() { 
        return "--- REPRODUCIENDO VIDEO ---\n" + getTitulo() + "\nDuración: " + duracion + "s\nURL: " + url;
    }

    @Override
    public String publicar() { 
        this.setEstado("Publicado");
        return "Video '" + getTitulo() + "' publicado en la sección estudiantil.";
    }
}