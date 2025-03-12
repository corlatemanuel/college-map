package Main;

import Service.*;
import Repo.*;
import Entity.*;

import javafx.application.Application;
import UI.*;
import JavaFaker.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Repository<Masina> masinaRepo = RepositoryFactory.createMasiniRepository();
        Repository<Inchiriere> inchiriereRepo = RepositoryFactory.createInchirieriRepository();

        ServiciuMasina serviciuMasina = new ServiciuMasina(masinaRepo);
        ServiciuInchiriere serviciuInchiriere = new ServiciuInchiriere(inchiriereRepo);

        //Faker
//        DataGenerator generator = new DataGenerator();
//        try {
//            generator.generateAndSaveData();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        //UI ui = new UI(serviciuMasina, serviciuInchiriere); ui.start();
        GUI gui = new GUI(serviciuMasina, serviciuInchiriere); Application.launch(GUI.class, args);
    }
}