package GUI;

import Entity.Obiect1;
import Repo.Repository;
import Repo.RepositoryFactory;
import Service.ServiceObiect1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

public class GUI extends Application {
    private ServiceObiect1 serviceObiect1;

    // Constructorul implicit pentru JavaFX
    public GUI() {
    }

    // Constructorul cu parametri pentru injectarea dependențelor
    public GUI(ServiceObiect1 serviceObiect1) {
        this.serviceObiect1 = serviceObiect1;
    }

    @Override
    public void start(Stage primaryStage) {
        if (serviceObiect1 == null) {
            Repository<Obiect1> entRepo = RepositoryFactory.createRepository("sql","");
            serviceObiect1 = new ServiceObiect1(entRepo);
        }

        primaryStage.setTitle("Gestionare Piese");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(10));

        Button btnObiecte = new Button("Gestionează Piese");
        Button btnFiltrareObiecte = new Button("Filtrare Piese");

        menu.getChildren().addAll(btnObiecte, btnFiltrareObiecte);

        root.setLeft(menu);

        StackPane centerPane = new StackPane();
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnObiecte.setOnAction(e -> gestioneazaObiecte(centerPane));
        btnFiltrareObiecte.setOnAction(e -> filtreazaObiectele(centerPane));
    }

    private void filtreazaObiectele(StackPane centerPane) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Filtrare piese");
        ListView<String> listView = new ListView<>();

        HBox form = new HBox(10);
        TextField filterfield = new TextField();
        filterfield.setPromptText("Filtreaza dupa...");
        Button filterButton = new Button("Cauta");
        Button resetButton = new Button("Resetare");
        form.getChildren().addAll(filterfield, filterButton, resetButton);

        filterButton.setOnAction(event -> {
            listView.getItems().clear();
            String filterText = filterfield.getText();
            var rezultate = serviceObiect1.filtreazaDupa(filterText);

            if (rezultate.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eroare");
                alert.setHeaderText("Niciun rezultat");
                alert.setContentText("Nu exista rezultate pentru filtrarea efectuata.");
                alert.showAndWait();
            } else {
                rezultate.forEach(m -> listView.getItems().add(m.toString()));
            }
        });

        resetButton.setOnAction(event -> {
            updateObiecteListView(listView);
            filterfield.clear();
        });


        pane.getChildren().addAll(title, listView, form);
        centerPane.getChildren().setAll(pane);
    }


    private void gestioneazaObiecte(StackPane centerPane) {
        VBox obiectePane = new VBox(10);
        obiectePane.setPadding(new Insets(10));

        Label title = new Label("Gestionează Piese");
        ListView<String> listView = new ListView<>();
        updateObiecteListView(listView);

        // Form pentru adăugare mașină
        HBox form = new HBox(10);
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField formatieField = new TextField();
        formatieField.setPromptText("Formatie");
        TextField titluField = new TextField();
        titluField.setPromptText("Titlu");
        TextField genField = new TextField();
        genField.setPromptText("Gen muzical");
        TextField durataField = new TextField();
        durataField.setPromptText("Durata");
        Button btnAdd = new Button("Adaugă");
        Button btnEdit = new Button("Modifică");
        form.getChildren().addAll(idField, formatieField, titluField, genField, durataField, btnAdd, btnEdit);

        // Buton pentru ștergere mașină
        Button btnDelete = new Button("Șterge Piesa");
        TextField deleteField = new TextField();
        deleteField.setPromptText("ID pentru ștergere");

        // Handlere pentru acțiuni
        btnAdd.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String formatie = formatieField.getText();
                String titlu = titluField.getText();
                String gen = genField.getText();
                String durata = durataField.getText();
                serviceObiect1.adaugaEntitate(new Obiect1(id, formatie, titlu, gen, durata));
                updateObiecteListView(listView);
                idField.clear();
                formatieField.clear();
                titluField.clear();
                genField.clear();
                durataField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnEdit.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String formatie = formatieField.getText();
                String titlu = titluField.getText();
                String gen = genField.getText();
                String durata = durataField.getText();
                serviceObiect1.modificaEntitatea(new Obiect1(id, formatie, titlu, gen, durata));
                updateObiecteListView(listView);
                idField.clear();
                formatieField.clear();
                titluField.clear();
                genField.clear();
                durataField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnDelete.setOnAction(e -> {
            try {
                int id = Integer.parseInt(deleteField.getText());
                serviceObiect1.stergeEntitatea(id);
                updateObiecteListView(listView);
                deleteField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        obiectePane.getChildren().addAll(title, listView, form, deleteField, btnDelete);
        centerPane.getChildren().setAll(obiectePane);
    }

    private void updateObiecteListView(ListView<String> listView) {
        listView.getItems().clear();
        List<Obiect1> obiecte = serviceObiect1.toateEntitatile();

        obiecte.sort(Comparator.comparing(Obiect1::getFormatie));

        obiecte.forEach(m -> listView.getItems().add(m.toString()));
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
