package Repo;

import Entity.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class RepositoryFactory {

    private static final String SETTINGS_FILE = "settings.properties";

    public static Repository<Masina> createMasiniRepository() {
        Properties properties = loadProperties();
        if (properties == null) return null;

        String repositoryType = properties.getProperty("Repository");

        if ("sql".equalsIgnoreCase(repositoryType)) {
            return createSqlMasinaRepository(properties);
        }

        String fileName = properties.getProperty("Masini");
        ensureFileExists(fileName);

        if ("text".equalsIgnoreCase(repositoryType)) {
            return new TextFileRepository<>(fileName);
        } else if ("binary".equalsIgnoreCase(repositoryType)) {
            return new BinaryFileRepository<>(fileName);
        } else {
            throw new IllegalArgumentException("Tip de repository necunoscut: " + repositoryType);
        }
    }

    public static Repository<Inchiriere> createInchirieriRepository() {
        Properties properties = loadProperties();
        if (properties == null) return null;

        String repositoryType = properties.getProperty("Repository");

        if ("sql".equalsIgnoreCase(repositoryType)) {
            return createSqlInchiriereRepository(properties);
        }

        String fileName = properties.getProperty("Inchirieri");
        ensureFileExists(fileName);

        if ("text".equalsIgnoreCase(repositoryType)) {
            return new TextFileRepository<>(fileName);
        } else if ("binary".equalsIgnoreCase(repositoryType)) {
            return new BinaryFileRepository<>(fileName);
        } else {
            throw new IllegalArgumentException("Tip de repository necunoscut: " + repositoryType);
        }
    }

    private static Repository<Masina> createSqlMasinaRepository(Properties properties) {
        try {
            Connection connection = createSqlConnection(properties);
            return new SqlRepository<>(connection, "masini");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Nu s-a putut crea conexiunea la baza de date pentru Masina", e);
        }
    }

    private static Repository<Inchiriere> createSqlInchiriereRepository(Properties properties) {
        try {
            Connection connection = createSqlConnection(properties);
            return new SqlRepository<>(connection, "inchirieri");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Nu s-a putut crea conexiunea la baza de date pentru Inchiriere", e);
        }
    }

    private static Connection createSqlConnection(Properties properties) throws SQLException {
        Connection connection = null;
        try {
            // Încărcați driver-ul SQLite JDBC
            Class.forName("org.sqlite.JDBC");

            // Stabiliți conexiunea la baza de date SQLite (fișierul DB)
            String url = properties.getProperty("DbUrl"); // Înlocuiți cu calea către fișierul .db
            connection = DriverManager.getConnection(url);

            System.out.println("Conexiune stabilită cu succes!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Eroare la conexiune: " + e.getMessage());
        }
        createTables(connection);
        return connection;
    }

    public static void createTables(Connection connection) throws SQLException {
        String createMasiniTable = "CREATE TABLE IF NOT EXISTS masini (" +
                "id INTEGER PRIMARY KEY," +
                "marca TEXT," +
                "model TEXT" +
                ");";

        String createInchirieriTable = "CREATE TABLE IF NOT EXISTS inchirieri (" +
                "id INTEGER PRIMARY KEY," +
                "masina_id INTEGER," +
                "data_inceput DATE," +
                "data_sfarsit DATE," +
                "FOREIGN KEY(masina_id) REFERENCES masini(id)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createMasiniTable);
            stmt.execute(createInchirieriTable);
        } catch (SQLException e) {
            System.out.println("Eroare la crearea tabelelor: " + e.getMessage());
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(SETTINGS_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    private static void ensureFileExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Fișierul " + fileName + " a fost creat.");
                }
            } catch (IOException e) {
                System.err.println("Eroare la crearea fișierului: " + fileName);
                e.printStackTrace();
            }
        }
    }
}
