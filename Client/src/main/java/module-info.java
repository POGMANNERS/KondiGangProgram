module managementsystem.gonnawork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires com.google.gson;

    opens managementsystem.client to javafx.fxml, com.google.gson; // Open the package to Gson module and JavaFX and its submodules for reflection and dependency injection to work properly
    exports managementsystem.client;
}