module hse.java.commander {
    requires javafx.controls;
    requires javafx.fxml;

    opens hse.java.commander to javafx.fxml;
    exports hse.java.commander;
}