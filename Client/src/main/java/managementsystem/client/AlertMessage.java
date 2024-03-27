package managementsystem.client;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertMessage {

    private Alert alert;

    public void successMessage(String message) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void errorMessage(String message) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public boolean confirmMessage(String message) {
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();

        return option.get().equals(ButtonType.OK);
    }
}