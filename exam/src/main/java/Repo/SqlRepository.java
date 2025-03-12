package Repo;

import Entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlRepository<T extends Entity> implements Repository<T> {
    private final Connection connection;

    private final String entityName;

    public SqlRepository(Connection connection, String entityClass) {
        this.connection = connection;
        this.entityName = entityClass;
    }

    @Override
    public void add(T entity) throws SQLException {
        if (entity instanceof Obiect1) {
            addEnt((Obiect1) entity);
        }
    }

    @Override
    public Optional<T> findById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }

        // Căutăm în funcție de tipul de entitate
        String sql = "SELECT * FROM " + entityName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (id == 1) {  // Presupunem că "Obiect1" este tabelul 1
                    Obiect1 ent = new Obiect1(rs.getInt("id"), rs.getString("formatie"), rs.getString("titlu"), rs.getString("gen"), rs.getString("durata"));
                    return Optional.of((T) ent);
                }
            }
        } catch (SQLException e) {
            // Aruncă o excepție runtime personalizată sau o excepție mai generală
            throw new RuntimeException("Database error during findById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        String tableName = entityName;

        String sql = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (tableName.equals("obiecte")) {
                    Obiect1 ent = new Obiect1(
                            rs.getInt("id"),
                            rs.getString("formatie"),
                            rs.getString("titlu"),
                            rs.getString("gen"),
                            rs.getString("durata")
                    );
                    entities.add((T) ent);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll", e);
        }
        return entities;
    }

    private Obiect1 findEntById(int entId) throws SQLException {
        String sql = "SELECT * FROM obiecte WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Obiect1(
                        rs.getInt("id"),
                        rs.getString("formatie"),
                        rs.getString("titlu"),
                        rs.getString("gen"),
                        rs.getString("durata")
                );
            }
        }
        throw new SQLException("Obiectul cu ID-ul " + entId + " nu a fost găsită.");
    }

    @Override
    public void update(T entity) throws SQLException {
        if (entity instanceof Obiect1) {
            updateEnt((Obiect1) entity);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM " + entityName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private void addEnt(Obiect1 ent) throws SQLException {
        String sql = "INSERT INTO obiecte (id, formatie, titlu, gen, durata) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ent.getId());
            stmt.setString(2, ent.getFormatie());
            stmt.setString(3, ent.getTitlu());
            stmt.setString(4, ent.getGen());
            stmt.setString(5, ent.getDurata());
            stmt.executeUpdate();
        }
    }

    private void updateEnt(Obiect1 ent) throws SQLException {
        String sql = "UPDATE obiecte SET id = ?, formatie = ?, titlu = ?, gen = ?, durata = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ent.getId());
            stmt.setString(2, ent.getFormatie());
            stmt.setString(3, ent.getTitlu());
            stmt.setString(4, ent.getGen());
            stmt.setString(5, ent.getDurata());
            stmt.executeUpdate();
        }
    }

}
