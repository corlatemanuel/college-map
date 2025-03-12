package Main;

import Service.*;
import Repo.*;
import Entity.*;

import javafx.application.Application;
import GUI.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Repository<Obiect1> entRepo = RepositoryFactory.createRepository("sql", "");

        ServiceObiect1 serviceObiect1 = new ServiceObiect1(entRepo);

        GUI gui = new GUI(serviceObiect1); Application.launch(GUI.class, args);
    }
}