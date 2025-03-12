package JavaFaker;

import Entity.*;
import Repo.SqlRepository;
import Repo.RepositoryFactory;
import com.github.javafaker.Faker;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DataGenerator {

    private Connection connection;
    private SqlRepository<Masina> masinaRepository;
    private SqlRepository<Inchiriere> inchiriereRepository;
    private Faker faker;

    public DataGenerator() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:faker.sqlite");
            ;
            this.masinaRepository = new SqlRepository<>(connection, "masini");
            this.inchiriereRepository = new SqlRepository<>(connection, "inchirieri");
            this.faker = new Faker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateAndSaveData() throws SQLException {
        Repo.RepositoryFactory.createTables(connection);
        List<Masina> masini = generateMasini(100); // Generăm 100 de mașini
        saveMasini(masini);

        List<Inchiriere> inchirieri = generateInchirieri(masini, 100); // Generăm 100 de închirieri
        saveInchirieri(inchirieri);
    }

    // Funcție pentru a genera 100 de mașini aleatoare
    private List<Masina> generateMasini(int count) {
        List<Masina> masini = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Masina masina = new Masina(
                    i + 1,  // ID-ul mașinii
                    faker.company().name(),  // Numele mărcii
                    faker.lorem().word()  // Modelul
            );
            masini.add(masina);
        }
        return masini;
    }

    // Funcție pentru a salva mașinile generate în baza de date
    private void saveMasini(List<Masina> masini) throws SQLException {
        for (Masina masina : masini) {
            masinaRepository.add(masina);  // Folosind repository-ul pentru a salva mașina
        }
    }

    // Funcție pentru a genera 100 de închirieri aleatoare
    private List<Inchiriere> generateInchirieri(List<Masina> masini, int count) {
        List<Inchiriere> inchirieri = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Masina masina = masini.get(faker.number().numberBetween(0, masini.size()));
            Inchiriere inchiriere = new Inchiriere(
                    i + 1,  // ID-ul închirierii
                    masina,
                    faker.date().past(30, java.util.concurrent.TimeUnit.DAYS),  // Data început aleatoare
                    faker.date().future(30, java.util.concurrent.TimeUnit.DAYS)  // Data sfârșit aleatoare
            );
            inchirieri.add(inchiriere);
        }
        return inchirieri;
    }

    // Funcție pentru a salva închirierile generate în baza de date
    private void saveInchirieri(List<Inchiriere> inchirieri) throws SQLException {
        for (Inchiriere inchiriere : inchirieri) {
            inchiriereRepository.add(inchiriere);  // Folosind repository-ul pentru a salva închirierea
        }
    }
}
