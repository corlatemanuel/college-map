package Repo;

import Entity.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RepositoryFactory {

    public static Repository<Obiect1> createRepository(String repositoryType, String fileName) {


            return createSqlRepository("jdbc:sqlite:data.sqlite");


    }

    private static Repository<Obiect1> createSqlRepository(String DbUrl) {
        try {
            Connection connection = createSqlConnection(DbUrl);
            return new SqlRepository<Obiect1>(connection, "obiecte");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Nu s-a putut crea conexiunea la baza de date pentru Masina", e);
        }
    }

    private static Connection createSqlConnection(String DbUrl) throws SQLException {
        Connection connection = null;
        try {
            // Încărcați driver-ul SQLite JDBC
            Class.forName("org.sqlite.JDBC");

            // Stabiliți conexiunea la baza de date SQLite (fișierul DB)
            String url = DbUrl; // Înlocuiți cu calea către fișierul .db
            connection = DriverManager.getConnection(url);

            System.out.println("Conexiune stabilită cu succes!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Eroare la conexiune: " + e.getMessage());
        }
        createTables(connection);
        return connection;
    }

    public static void createTables(Connection connection) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS obiecte (" +
                "id INTEGER PRIMARY KEY," +
                "formatie TEXT," +
                "titlu TEXT," +
                "gen TEXT," +
                "durata TEXT" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.out.println("Eroare la crearea tabelului: " + e.getMessage());
        }
    }

}
