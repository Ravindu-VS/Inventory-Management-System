module com.inventory.management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;

    opens com.inventory.management.ui to javafx.fxml;
    opens com.inventory.management.model to com.google.gson;

    exports com.inventory.management;
    exports com.inventory.management.ui;
    exports com.inventory.management.model;
}
