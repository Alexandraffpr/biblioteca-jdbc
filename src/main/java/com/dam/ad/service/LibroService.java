package com.dam.ad.service;
import com.dam.ad.model.Libro;
import com.dam.ad.repository.LibroRepository;
import java.util.List;

public class LibroService {
    private final LibroRepository repo; // 1. Declaramos la dependencia

    public LibroService(LibroRepository repo) { // 2. La recibimos por el constructor
        this.repo = repo;
    }

    public List<Libro> listar() {
        return repo.findAll();
    }

    public Libro verDetalle(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe libro con id=" + id)); //
    }

    public int alta(Libro libro) {
        // REGLAS DE NEGOCIO: Antes de guardar, validamos
        if (libro.getTitulo() == null || libro.getTitulo().isBlank())
            throw new IllegalArgumentException("Título obligatorio");
        if (libro.getIsbn() == null || libro.getIsbn().isBlank())
            throw new IllegalArgumentException("ISBN obligatorio");

        return repo.insert(libro);
    }

    public void marcarNoDisponible(int id) {
        Libro l = verDetalle(id); // Buscamos primero
        l.setDisponible(false);   // Cambiamos el estado
        repo.update(l);           // Guardamos el cambio
    }

    public void borrar(int id) {
        boolean ok = repo.deleteById(id);
        if (!ok) throw new IllegalArgumentException("No existe id=" + id); //
    }
}