module com.example.sanmarino {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.sanmarino to javafx.fxml;
    exports com.example.sanmarino;
}