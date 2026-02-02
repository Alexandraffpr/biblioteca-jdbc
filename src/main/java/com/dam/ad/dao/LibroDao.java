package com.dam.ad.dao;

import com.dam.ad.db.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDao {

    public List<com.dam.ad.model.Libro> findAll() {
        // Asegúrate de que esta línea termine en ";
        String sql = "SELECT id, titulo, isbn, anio, disponible FROM libro ORDER BY id";
        List<com.dam.ad.model.Libro> libros = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(new com.dam.ad.model.Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("isbn"),
                        (Integer) rs.getObject("anio"),
                        rs.getBoolean("disponible")
                ));
            }
        } catch (SQLException e) {
            // Esto te dirá en la consola si hay un error con la base de datos
            throw new RuntimeException("Error al leer libros", e);
        }
        return libros;
    }
}
