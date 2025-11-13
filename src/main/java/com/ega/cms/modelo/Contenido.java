package com.ega.cms.modelo;

import java.time.LocalDate;
import java.util.ArrayList;

// Contenido.java
// Descripción: Clase base abstracta. Ahora incluye estado, categoría y etiquetas.

public abstract class Contenido implements Comparable<Contenido>
{
    private int id;
    private String titulo;
    private Usuario autor;
    private LocalDate fechaPublicacion;
    private String categoria;
    private String estado;
    private int vistas;
    private ArrayList<String> etiquetas;

    public Contenido(int id, String titulo, Usuario autor, String categoria)
    {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.fechaPublicacion = LocalDate.now();
        this.estado = "Borrador";
        this.vistas = 0;
        this.etiquetas = new ArrayList<>();
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public Usuario getAutor() { return autor; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public String getCategoria() { return categoria; }
    public String getEstado() { return estado; }
    public int getVistas() { return vistas; }
    public String getEtiquetasString()
    {
        return String.join(", ", etiquetas);
    }

    public abstract String getTipo();
    
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void addEtiqueta(String etiqueta) { this.etiquetas.add(etiqueta); }

    @Override
    public String toString()
    {
        return "ID: " + id + " | Tipo: " + getTipo() + " | Título: " + titulo;
    }

    @Override
    public int compareTo(Contenido otro)
    {
        return otro.getFechaPublicacion().compareTo(this.fechaPublicacion);
    }
}