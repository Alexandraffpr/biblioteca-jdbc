package com.dam.ad.model;

public class Libro {
    private Integer id; // Ahora puede ser null antes del INSERT
    private String titulo;
    private String isbn;
    private Integer anio;
    private boolean disponible;

    // Constructor para libros que ya existen (con ID) [cite: 1030-1031]
    public Libro(Integer id, String titulo, String isbn, Integer anio, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.anio = anio;
        this.disponible = disponible;
    }
    // Constructor para libros nuevos (sin ID)
    public Libro(String titulo, String isbn, Integer anio, boolean disponible) {
        this(null, titulo, isbn, anio, disponible); // El ID nace como null [cite: 1042]
    }
    // Getters y Setters necesarios para el DAO [cite: 1044-1053, 1071]
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", titulo='" + titulo + '\'' +
                ", isbn='" + isbn + '\'' + ", anio=" + anio +
                ", disponible=" + disponible + '}';
    }
}