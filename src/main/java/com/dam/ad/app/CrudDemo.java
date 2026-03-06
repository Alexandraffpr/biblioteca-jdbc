package com.dam.ad.app;
import com.dam.ad.model.Libro;
import com.dam.ad.repository.LibroRepository;
import com.dam.ad.repository.jdbc.JdbcLibroRepository;
import com.dam.ad.service.LibroService;

public class CrudDemo {
    public static void main(String[] args) {
        // 1. Conectamos las piezas (Inyección)
        LibroRepository repo = new JdbcLibroRepository();
        LibroService service = new LibroService(repo);

        // 2. Ejecutamos los casos de uso a través del Service
        System.out.println("=== LISTADO ===");
        service.listar().forEach(System.out::println);

        System.out.println("\n=== ALTA ===");
        Libro nuevo = new Libro("Clean Code", "9780132350884-X", 2008, true);
        int id = service.alta(nuevo);
        System.out.println("Creado con éxito: " + nuevo);

        System.out.println("\n=== MARCAR NO DISPONIBLE ===");
        service.marcarNoDisponible(id);
        System.out.println(service.verDetalle(id));
    }
}