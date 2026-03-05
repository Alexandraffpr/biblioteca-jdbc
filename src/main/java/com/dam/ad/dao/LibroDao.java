package com.dam.ad.dao;
import com.dam.ad.db.Db;
import com.dam.ad.model.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroDao {

    // SUGERENCIA: Método "helper" para evitar duplicar código
    private Libro mapRow(ResultSet rs) throws SQLException {
        return new Libro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("isbn"),
                (Integer) rs.getObject("anio"),
                rs.getBoolean("disponible")
        );
    }
    public List<Libro> findAll() {
        String sql = "SELECT id, titulo, isbn, anio, disponible FROM libro ORDER BY id";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Libro> libros = new ArrayList<>();
            while (rs.next()) {
                libros.add(mapRow(rs)); // Usamos el helper [cite: 1191]
            }
            return libros;
        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo libros", e);
        }
    }

    // Implementación de findById
    public Optional<Libro> findById(int id) {
        String sql = "SELECT id, titulo, isbn, anio, disponible FROM libro WHERE id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando libro id=" + id, e);
        }
    }

    // Implementación de insert con captura de ID
    public int insert(Libro libro) {
        String sql = "INSERT INTO libro (titulo, isbn, anio, disponible) VALUES (?,?,?,?)";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getIsbn());
            ps.setObject(3, libro.getAnio());
            ps.setBoolean(4, libro.isDisponible());
            ps.executeUpdate();

            // Recuperamos el ID que PostgreSQL creó automáticamente
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    libro.setId(id);
                    return id;
                }
            }
            throw new SQLException("No se pudo obtener el ID generado");
        } catch (SQLException e) {
            throw new RuntimeException("Error insertando libro", e);
        }
    }

    // Implementación de update
    public boolean update(Libro libro) {
        String sql = "UPDATE libro SET titulo=?, isbn=?, anio=?, disponible=? WHERE id=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getIsbn());
            ps.setObject(3, libro.getAnio());
            ps.setBoolean(4, libro.isDisponible());
            ps.setInt(5, libro.getId());
            return ps.executeUpdate() == 1; // Devuelve true si se actualizó la fila
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando libro id=" + libro.getId(), e);
        }
    }

    // Implementación de deleteById
    public boolean deleteById(int id) {
        String sql = "DELETE FROM libro WHERE id=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1; // Devuelve true si se borró la fila
        } catch (SQLException e) {
            throw new RuntimeException("Error borrando libro id=" + id, e);
        }
    }

    // Nuevo metodo para buscar libros por una palabra en el título
    public List<Libro> searchByTitulo(String texto) {
        // Usamos LIKE para buscar patrones y LOWER para ignorar mayúsculas
        String sql = "SELECT id, titulo, isbn, anio, disponible FROM libro WHERE LOWER(titulo) LIKE ?";
        List<Libro> resultados = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // El % indica que puede haber cualquier texto antes o después
            // Ejemplo: "%Harry%" busca cualquier título que contenga "Harry"
            ps.setString(1, "%" + texto.toLowerCase() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Reutilizamos mapRow de la Práctica 2
                    resultados.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar por título", e);
        }
        return resultados;
    }


}
