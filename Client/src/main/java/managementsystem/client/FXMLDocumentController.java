package managementsystem.client;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class is the controller for the login form of the management system.
 * It handles the login process, including validation, sending login data to the server, and handling the server's response.
 */
public class FXMLDocumentController {

    @FXML
    private AnchorPane login_form;

    @FXML
    private TextField login_username;

    @FXML
    private PasswordField login_password;

    @FXML
    private Button login_btn;

    private AlertMessage alert = new AlertMessage();  // An instance of AlertMessage to display error messages

    // The URL of the server
    private final String serverURL = "http://localhost:5000";

    private double xOffset = 0;  // The x offset for dragging the window
    private double yOffset = 0;  // The y offset for dragging the window

    /**
     * This method handles the login process.
     * It validates the input, sends the login data to the server, and handles the server's response.
     * If the login is successful, it opens the main window and hides the login window.
     * If the login fails, it displays an error message.
     */
    public void loginAccount() {
        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields!");  // Display an error message if any field is empty
        } else {
            System.out.println("Login procedure with acquiring the Token and then setting it");
            try {
                HTTP_handler httpLogin = new HTTP_handler(serverURL,"/login");  // Create an instance of HTTP_handler for the login process
                httpLogin.startUp();  // Start the HTTP handler

                Gson gson = new Gson();  // Create an instance of Gson to convert Java objects into JSON and back
                J_login jl = new J_login(login_username.getText(), login_password.getText());  // Create an instance of J_login with the entered username and password

                httpLogin.cookieSend("Content-Type","application/json", gson.toJson(jl));  // Send the login data to the server
                System.out.println("Sent login data: " + gson.toJson(jl));

                if(httpLogin.getResponse().equals("200"))  // If the server's response is 200 (OK)
                {
                    HTTP_handler.setToken(httpLogin.tokenCutter(httpLogin.cookieRead("Set-Cookie")));  // Set the token
                    System.out.println(httpLogin.getResponse());
                    J_parts_modify_get.role = httpLogin.bodyRead();  // Set the role
                    System.out.println("Your role is: " + J_parts_modify_get.role + "!");
                    J_parts_modify_get.username = login_username.getText();  // Set the username
                } else {
                    System.out.println("A bad thing happened: " + httpLogin.getResponse());  // If the server's response is not 200 (OK), print an error message
                }
                httpLogin.stop();  // Stop the HTTP handler

                Thread.sleep(1000);  // Wait for 1 second

                if (J_parts_modify_get.role != null) {  // If the role is not null (i.e., the login was successful)
                    // Set main window
                    Parent root = FXMLLoader.load(getClass().getResource("AdminMainForm.fxml"));  // Load the main window

                    Stage stage = new Stage();  // Create a new stage for the main window
                    stage.initStyle(StageStyle.UNDECORATED);  // Set the stage style to UNDECORATED to remove the window buttons

                    // Make the window movable by mouse
                    root.setOnMousePressed(event -> {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                        stage.getScene().getRoot().setOpacity(0.8);  // Set window opacity to 80% while dragging
                    });

                    // Calculate the new window position while dragging
                    root.setOnMouseDragged(event -> {
                        stage.setX(event.getScreenX() - xOffset);
                        stage.setY(event.getScreenY() - yOffset);
                    });

                    // Restore window opacity when dragging is finished
                    root.setOnMouseReleased(event -> {
                        stage.getScene().getRoot().setOpacity(1.0);  // Restore window opacity to 100%
                    });

                    // Set the scene to the stage
                    stage.setScene(new Scene(root));

                    // Show the stage
                    stage.show();

                    // Hide login window
                    login_btn.getScene().getWindow().hide();

                } else {
                    alert.errorMessage("Incorrect Username/Password");  // If the role is null (i.e., the login failed), display an error message
                }
            }  catch (Exception e) {
                e.printStackTrace();  // Print the stack trace for any caught exceptions
            }
        }
    }

}