module lab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;
    requires java.sql;
    requires javafaker;

    opens UI to javafx.fxml;
    exports UI;
}