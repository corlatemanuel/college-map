package Repo;

import Entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlRepository<T extends Entitate> implements Repository<T> {
    private final Connection connection;

    private final String entityName;

    public SqlRepository(Connection connection, String entityClass) {
        this.connection = connection;
        this.entityName = entityClass;
    }

    @Override
    public void add(T entity) throws SQLException {
        if (entity instanceof Masina) {
            addMasina((Masina) entity);
        } else if (entity instanceof Inchiriere) {
            addInchiriere((Inchiriere) entity);
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
                if (id == 1) {  // Presupunem că "Masina" este tabelul 1
                    Masina masina = new Masina(rs.getInt("id"), rs.getString("marca"), rs.getString("model"));
                    return Optional.of((T) masina);
                } else {  // Presupunem că "Inchiriere" este tabelul 2
                    Masina masina = new Masina(rs.getInt("masina_id"), "", ""); // Exemplu, trebuie completat
                    Inchiriere inchiriere = new Inchiriere(rs.getInt("id"), masina, rs.getDate("data_inceput"), rs.getDate("data_sfarsit"));
                    return Optional.of((T) inchiriere);
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
                if (tableName.equals("masini")) {
                    Masina masina = new Masina(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("model")
                    );
                    entities.add((T) masina);
                } else if (tableName.equals("inchirieri")) {
                    Masina masina = findMasinaById(rs.getInt("masina_id"));
                    Inchiriere inchiriere = new Inchiriere(
                            rs.getInt("id"),
                            masina,
                            rs.getDate("data_inceput"),
                            rs.getDate("data_sfarsit")
                    );
                    entities.add((T) inchiriere);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll", e);
        }
        return entities;
    }

    private Masina findMasinaById(int masinaId) throws SQLException {
        String sql = "SELECT * FROM masini WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, masinaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Masina(
                        rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getString("model")
                );
            }
        }
        throw new SQLException("Masina cu ID-ul " + masinaId + " nu a fost găsită.");
    }

    @Override
    public void update(T entity) throws SQLException {
        if (entity instanceof Masina) {
            updateMasina((Masina) entity);
        } else if (entity instanceof Inchiriere) {
            updateInchiriere((Inchiriere) entity);
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

    private void addMasina(Masina masina) throws SQLException {
        String sql = "INSERT INTO masini (id, marca, model) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, masina.getId());
            stmt.setString(2, masina.getMarca());
            stmt.setString(3, masina.getModel());
            stmt.executeUpdate();
        }
    }

    private void addInchiriere(Inchiriere inchiriere) throws SQLException {
        String sql = "INSERT INTO inchirieri (id, masina_id, data_inceput, data_sfarsit) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, inchiriere.getId());
            stmt.setInt(2, inchiriere.getMasina().getId());
            stmt.setDate(3, new java.sql.Date(inchiriere.getDataInceput().getTime()));
            stmt.setDate(4, new java.sql.Date(inchiriere.getDataSfarsit().getTime()));
            stmt.executeUpdate();
        }
        //Cod vizualizare date in SQL:
//        SELECT id, masina_id,
//        date(data_inceput / 1000, 'unixepoch') AS data_inceput,
//        date(data_sfarsit / 1000, 'unixepoch') AS data_sfarsit
//        FROM inchirieri;
    }

    private void updateMasina(Masina masina) throws SQLException {
        String sql = "UPDATE masini SET marca = ?, model = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, masina.getMarca());
            stmt.setString(2, masina.getModel());
            stmt.setInt(3, masina.getId());
            stmt.executeUpdate();
        }
    }

    private void updateInchiriere(Inchiriere inchiriere) throws SQLException {
        String sql = "UPDATE inchirieri SET masina_id = ?, data_inceput = ?, data_sfarsit = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, inchiriere.getMasina().getId());
            stmt.setDate(2, new java.sql.Date(inchiriere.getDataInceput().getTime()));
            stmt.setDate(3, new java.sql.Date(inchiriere.getDataSfarsit().getTime()));
            stmt.setInt(4, inchiriere.getId());
            stmt.executeUpdate();
        }
    }

}
