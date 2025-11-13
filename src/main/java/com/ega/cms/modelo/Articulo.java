package com.ega.cms.modelo;

// Articulo.java
// Descripción: Subclase de Contenido. Implementa Publicable y la categoría Academico.

public class Articulo extends Contenido implements Publicable, Academico 
{
    private String texto;

    public Articulo(int id, String titulo, Usuario autor, String categoria, String texto)
    {
        super(id, titulo, autor, categoria);
        this.texto = texto;
    }

    public String getTexto(){ return this.texto; }

    @Override
    public String getTipo(){ return "Artículo"; }

    @Override
    public String mostrar() { return "--- MOSTRANDO ARTÍCULO ---\n" + getTitulo() + "\nPor: " + getAutor().getUsername() + "\n\n" + texto; }

    @Override
    public String publicar() { this.setEstado("Publicado"); return "Artículo '" + getTitulo() + "' publicado en el portal académico."; }
}