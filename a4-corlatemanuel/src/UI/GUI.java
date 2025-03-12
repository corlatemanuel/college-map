package UI;

import Entity.Masina;
import Entity.Inchiriere;
import Repo.Repository;
import Repo.RepositoryFactory;
import Service.ServiciuMasina;
import Service.ServiciuInchiriere;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GUI extends Application {
    private ServiciuMasina serviciuMasina;
    private ServiciuInchiriere serviciuInchiriere;

    // Constructorul implicit pentru JavaFX
    public GUI() {
    }

    // Constructorul cu parametri pentru injectarea dependențelor
    public GUI(ServiciuMasina serviciuMasina, ServiciuInchiriere serviciuInchiriere) {
        this.serviciuMasina = serviciuMasina;
        this.serviciuInchiriere = serviciuInchiriere;
    }

    @Override
    public void start(Stage primaryStage) {
        if (serviciuMasina == null) {
            Repository<Masina> masinaRepo = RepositoryFactory.createMasiniRepository();
            serviciuMasina = new ServiciuMasina(masinaRepo);
        }
        if (serviciuInchiriere == null) {
            Repository<Inchiriere> inchiriereRepo = RepositoryFactory.createInchirieriRepository();
            serviciuInchiriere = new ServiciuInchiriere(inchiriereRepo);
        }

        primaryStage.setTitle("Gestionare Mașini și Închirieri");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(10));

        Button btnMasini = new Button("Gestionează Mașini");
        Button btnInchirieri = new Button("Gestionează Închirieri");
        Button btnCeleMaiDesInchiriate = new Button("Cele mai des închiriate mașini");
        Button btnInchirieriPeLuna = new Button("Număr închirieri pe lună");
        Button btnZileInchiriere = new Button("Mașinile cu cele mai multe zile de închiriere");

        menu.getChildren().addAll(btnMasini, btnInchirieri, btnCeleMaiDesInchiriate, btnInchirieriPeLuna, btnZileInchiriere);

        root.setLeft(menu);

        StackPane centerPane = new StackPane();
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnMasini.setOnAction(e -> gestioneazaMasini(centerPane));
        btnInchirieri.setOnAction(e -> gestioneazaInchirieri(centerPane));
        btnCeleMaiDesInchiriate.setOnAction(e -> afiseazaCeleMaiDesInchiriateMasini(centerPane));
        btnInchirieriPeLuna.setOnAction(e -> afiseazaInchirieriPeLuna(centerPane));
        btnZileInchiriere.setOnAction(e -> afiseazaMasinileInchiriateCelMaiMultTimp(centerPane));
    }

    private void afiseazaCeleMaiDesInchiriateMasini(StackPane centerPane) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Cele mai des închiriate mașini");
        ListView<String> listView = new ListView<>();

        serviciuInchiriere.celeMaiDesInchiriateMasini()
                .forEach((masina, numarInchirieri) ->
                        listView.getItems().add("Mașina: " + masina + ", Număr închirieri: " + numarInchirieri)
                );

        pane.getChildren().addAll(title, listView);
        centerPane.getChildren().setAll(pane);
    }

    private void afiseazaInchirieriPeLuna(StackPane centerPane) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Număr închirieri pe lună");
        ListView<String> listView = new ListView<>();

        serviciuInchiriere.numarInchirieriPeLuna()
                .forEach((luna, numarInchirieri) ->
                        listView.getItems().add("Luna: " + luna + ", Număr închirieri: " + numarInchirieri)
                );

        pane.getChildren().addAll(title, listView);
        centerPane.getChildren().setAll(pane);
    }

    private void afiseazaMasinileInchiriateCelMaiMultTimp(StackPane centerPane) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Mașinile cu cele mai multe zile de închiriere");
        ListView<String> listView = new ListView<>();

        serviciuInchiriere.masiniInchiriateCelMaiMultTimp()
                .forEach((masina, zile) ->
                        listView.getItems().add("Mașina: " + masina + ", Zile închiriate: " + zile)
                );

        pane.getChildren().addAll(title, listView);
        centerPane.getChildren().setAll(pane);
    }


    private void gestioneazaMasini(StackPane centerPane) {
        VBox masiniPane = new VBox(10);
        masiniPane.setPadding(new Insets(10));

        Label title = new Label("Gestionează Mașini");
        ListView<String> listView = new ListView<>();
        updateMasiniListView(listView);

        // Form pentru adăugare mașină
        HBox form = new HBox(10);
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField marcaField = new TextField();
        marcaField.setPromptText("Marca");
        TextField modelField = new TextField();
        modelField.setPromptText("Model");
        Button btnAdd = new Button("Adaugă");
        Button btnEdit = new Button("Modifică");
        form.getChildren().addAll(idField, marcaField, modelField, btnAdd, btnEdit);

        // Buton pentru ștergere mașină
        Button btnDelete = new Button("Șterge Mașină");
        TextField deleteField = new TextField();
        deleteField.setPromptText("ID pentru ștergere");

        // Handlere pentru acțiuni
        btnAdd.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String marca = marcaField.getText();
                String model = modelField.getText();
                serviciuMasina.adaugaMasina(new Masina(id, marca, model));
                updateMasiniListView(listView);
                idField.clear();
                marcaField.clear();
                modelField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnEdit.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String marca = marcaField.getText();
                String model = modelField.getText();
                serviciuMasina.modificaMasina(new Masina(id, marca, model));
                updateMasiniListView(listView);
                idField.clear();
                marcaField.clear();
                modelField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnDelete.setOnAction(e -> {
            try {
                int id = Integer.parseInt(deleteField.getText());
                serviciuMasina.stergeMasina(id);
                updateMasiniListView(listView);
                deleteField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        masiniPane.getChildren().addAll(title, listView, form, deleteField, btnDelete);
        centerPane.getChildren().setAll(masiniPane);
    }

    private void gestioneazaInchirieri(StackPane centerPane) {
        VBox inchirieriPane = new VBox(10);
        inchirieriPane.setPadding(new Insets(10));

        Label title = new Label("Gestionează Închirieri");
        ListView<String> listView = new ListView<>();
        updateInchirieriListView(listView);

        // Form pentru adăugare închiriere
        HBox form = new HBox(10);
        TextField idField = new TextField();
        idField.setPromptText("ID Închiriere");
        TextField idMasinaField = new TextField();
        idMasinaField.setPromptText("ID Mașină");
        TextField inceputField = new TextField();
        inceputField.setPromptText("Data Început (dd/MM/yyyy)");
        TextField sfarsitField = new TextField();
        sfarsitField.setPromptText("Data Sfârșit (dd/MM/yyyy)");
        Button btnAdd = new Button("Adaugă");
        Button btnEdit = new Button("Modifică");
        form.getChildren().addAll(idField, idMasinaField, inceputField, sfarsitField, btnAdd, btnEdit);

        // Buton pentru ștergere închiriere
        Button btnDelete = new Button("Șterge Închiriere");
        TextField deleteField = new TextField();
        deleteField.setPromptText("ID pentru ștergere");

        // Handlere pentru acțiuni
        btnAdd.setOnAction(e -> {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                int id = Integer.parseInt(idField.getText());
                int idMasina = Integer.parseInt(idMasinaField.getText());
                Date dataInceput = format.parse(inceputField.getText());
                Date dataSfarsit = format.parse(sfarsitField.getText());

                Masina masina = serviciuMasina.toateMasinile().stream()
                        .filter(m -> m.getId() == idMasina).findFirst().orElse(null);

                if (masina == null) {
                    throw new Exception("Mașina nu există!");
                }

                serviciuInchiriere.adaugaInchiriere(new Inchiriere(id, masina, dataInceput, dataSfarsit));
                updateInchirieriListView(listView);
                idField.clear();
                idMasinaField.clear();
                inceputField.clear();
                sfarsitField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnEdit.setOnAction(e -> {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                int id = Integer.parseInt(idField.getText());
                int idMasina = Integer.parseInt(idMasinaField.getText());
                Date dataInceput = format.parse(inceputField.getText());
                Date dataSfarsit = format.parse(sfarsitField.getText());

                Masina masina = serviciuMasina.toateMasinile().stream()
                        .filter(m -> m.getId() == idMasina).findFirst().orElse(null);

                if (masina == null) {
                    throw new Exception("Mașina nu există!");
                }

                Inchiriere inchiriereModificata = new Inchiriere(id, masina, dataInceput, dataSfarsit);
                serviciuInchiriere.modificaInchiriere(inchiriereModificata);
                updateInchirieriListView(listView);
                idField.clear();
                idMasinaField.clear();
                inceputField.clear();
                sfarsitField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnDelete.setOnAction(e -> {
            try {
                int id = Integer.parseInt(deleteField.getText());
                serviciuInchiriere.stergeInchiriere(id);
                updateInchirieriListView(listView);
                deleteField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        inchirieriPane.getChildren().addAll(title, listView, form, deleteField, btnDelete);
        centerPane.getChildren().setAll(inchirieriPane);
    }

    private void updateMasiniListView(ListView<String> listView) {
        listView.getItems().clear();
        List<Masina> masini = serviciuMasina.toateMasinile();
        masini.forEach(m -> listView.getItems().add(m.toString()));
    }

    private void updateInchirieriListView(ListView<String> listView) {
        listView.getItems().clear();
        List<Inchiriere> inchirieri = serviciuInchiriere.toateInchirierile();
        inchirieri.forEach(i -> listView.getItems().add(i.toString()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Se lansează aplicația JavaFX cu dependențele injectate
        launch(args);
    }
}
