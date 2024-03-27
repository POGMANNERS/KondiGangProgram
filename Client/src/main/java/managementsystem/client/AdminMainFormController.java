package managementsystem.client;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class is the controller for the main form of the management system.
 * It handles the display of data, communication with the server, and user interactions.
 */
public class AdminMainFormController implements Initializable {

    @FXML
    private Label greet_username;

    @FXML
    public Label user_role;

    @FXML
    private Label date_time;

    @FXML
    private Button logout_btn;

    @FXML
    public Button addUser_btn;

    @FXML
    public AnchorPane addUser_form;

    @FXML
    public ComboBox<String> addUser_roleSelector;

    @FXML
    public TextField addUser_username;

    @FXML
    public PasswordField addUser_password1;

    @FXML
    public PasswordField addUser_password2;

    @FXML
    public Button addUser_addBtn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_TProjects;

    @FXML
    private Label dashboard_TParts;

    @FXML
    private Label dashboard_OProjects;

    @FXML
    private BarChart<?, ?> dashboard_chartProjects;

    @FXML
    private BarChart<?, ?> dashboard_chartParts;

    @FXML
    private Button getParts_btn;

    @FXML
    private AnchorPane parts_form;

    @FXML
    private TextField addParts_name;

    @FXML
    private TextField addParts_price;

    @FXML
    private Spinner<Integer> addParts_quantitySpnr;

    @FXML
    private Button addParts_addBtn;

    @FXML
    private Button addParts_modifyBtn;

    @FXML
    private Button addParts_clearBtn;

    @FXML
    private TableView<J_parts_modify_get> Parts_tableView;

    @FXML
    private TableColumn<J_parts_modify_get, Id> Parts_col_code;

    @FXML
    private TableColumn<J_parts_modify_get, String> Parts_col_name;

    @FXML
    private TableColumn<J_parts_modify_get, Float> Parts_col_price;

    @FXML
    private TableColumn<J_parts_modify_get, Integer> Parts_col_maxPerUnit;

    // The URL of the server
    private final String serverURL = "http://localhost:5000";

    private Gson gson = new Gson();  // An instance of Gson to convert Java objects into JSON and back
    private AlertMessage alert = new AlertMessage();  // An instance of AlertMessage to display messages

    /**
     * This method ensures that only numeric input is allowed in the price field for parts.
     */
    public void addPartsPrice_NumOnly() {
        addParts_price.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
    }

    /**
     * This method displays the total number of parts on the dashboard.
     */
    public void dashboardDisplayTParts() {
        try {
            ObservableList<J_parts_modify_get> listData = addPartsGetData();
            dashboard_TParts.setText(String.valueOf(listData.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method generates a bar chart for the parts in the dashboard.
     */
    public void dashboardPartsChart() {
        dashboard_chartParts.getData().clear();

        try {
            XYChart.Series chart = new XYChart.Series<>();

            ObservableList<J_parts_modify_get> listData = addPartsGetData();
            for (J_parts_modify_get p : listData) {
                chart.getData().add(new XYChart.Data<>(p.getName(), p.getPrice()));
            }

            dashboard_chartParts.getData().add(chart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends a request to the server with the specified path and data.
     * @param path The path for the request.
     * @param data The data to be sent in the request.
     */
    public void communicateSend(String path, String data) {
        try {
            HTTP_handler httpSender = new HTTP_handler(serverURL, path);
            httpSender.startUp();
            httpSender.cookieSend("Content-Type", "application/json", data);
            System.out.println("[Sent data: " + data + " to path: " + path + "!");
            System.out.println(" Response: " + httpSender.bodyRead() + "]");
            httpSender.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends a request to the server with the specified path and returns the response data.
     * @param path The path for the request.
     * @return The response data from the server.
     */
    public ObservableList<J_parts_modify_get> communicateGet(String path) {
        ObservableList<J_parts_modify_get> listData = FXCollections.observableArrayList();
        try {
            HTTP_handler httpGetter = new HTTP_handler(serverURL, path);
            httpGetter.startUp();
            listData = FXCollections.observableArrayList(gson.fromJson(httpGetter.bodyRead(), J_parts_modify_get[].class));
            System.out.println("[Got data from path: " + path + "!]");
            httpGetter.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    /**
     * This method initializes the role selector for adding a new user.
     */
    public void addUser_roleSelectorInitialize() {
        ObservableList<String> roleList = FXCollections.observableArrayList("Professional", "Manager", "Operator");
        addUser_roleSelector.setItems(roleList);
    }

    /**
     * This method converts the selected role to its corresponding integer value.
     * @return The integer value of the selected role.
     */
    public int roleConvert() {
        int role = 0;
        switch (addUser_roleSelector.getValue()) {
            case "Professional":
                role = 1;
                break;
            case "Manager":
                role = 2;
                break;
            case "Operator":
                role = 3;
                break;
            default:
                break;
        }
        return role;
    }

    /**
     * This method handles the process of adding a new user.
     */
    public void addUserNew() {
        if (addUser_username.getText().isEmpty() || addUser_password1.getText().isEmpty() || addUser_password2.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields!");
        } else if (!addUser_password1.getText().equals(addUser_password2.getText())) {
            alert.errorMessage("Passwords do not match!");
        } else if (addUser_roleSelector.getValue() == null){
            alert.errorMessage("Please select a role!");
        } else {
            if (alert.confirmMessage("Are you sure you want to ADD the user: \"" + addUser_username.getText() + "\" with the role: " + addUser_roleSelector.getValue() + "?")) {
                try {
                    HTTP_handler httpSender = new HTTP_handler(serverURL, "/users-new");
                    httpSender.startUp();
                    httpSender.cookieSend("Content-Type", "application/json", gson.toJson(new J_users_new(addUser_username.getText(), addUser_password1.getText(), roleConvert(), "name")));
                    System.out.println(new J_users_new(addUser_username.getText(), addUser_password1.getText(), roleConvert(), "name"));
                    String response = httpSender.getResponse();
                    if (response.equals("200")) {
                        System.out.println("Added the user: " + addUser_username.getText() + " with the role: " + addUser_roleSelector.getValue() + "!");
                        alert.successMessage("Added new user successfully!");
                        addUserClearFields();
                    } else if (response.equals("409")) {
                        alert.errorMessage("A user named \"" + addUser_username.getText() + "\" already exists!");
                    } else {
                        alert.errorMessage("Something went wrong! Please try again later!");
                        System.out.println(response);
                        System.out.println(httpSender.bodyRead());
                    }
                    httpSender.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method clears the fields for adding a new user.
     */
    private void addUserClearFields() {
        addUser_username.clear();
        addUser_password1.clear();
        addUser_password2.clear();
    }

    /**
     * This method sends a request to the server to get the data of parts.
     * @return The data of parts from the server.
     */
    public ObservableList<J_parts_modify_get> addPartsGetData() {
        return communicateGet("/parts-get");
    }

    /**
     * This method displays the data of parts in the table view.
     */
    public void addPartsDisplayData() {
        Parts_col_code.setCellValueFactory(new PropertyValueFactory<>("id"));
        Parts_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Parts_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        Parts_col_maxPerUnit.setCellValueFactory(new PropertyValueFactory<>("maxnum"));

        Parts_tableView.setItems(addPartsGetData());
    }

    private String partID; // The ID of the selected part

    /**
     * This method handles the selection of an item in the parts table view.
     */
    public void addPartsSelectItem() {
        J_parts_modify_get sData = Parts_tableView.getSelectionModel().getSelectedItem();
        int num = Parts_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        addParts_name.setText(sData.getName());
        addParts_price.setText(String.valueOf(sData.getPrice()));
        addParts_quantitySpnr.getValueFactory().setValue(sData.getMaxnum());

        partID = sData.getId();
    }

    /**
     * This method initializes the spinner for selecting the quantity of a part.
     */
    public void addPartsSpnrInitialize() {
        addParts_quantitySpnr.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        //addParts_quantitySpnr.setEditable(true); //ha erre igény van akkor be kell állítani, hogy csak számokat lehessen beírni
    }

    /**
     * This method handles the process of adding a new part.
     */
    public void addPartsNew() {
        if (addParts_name.getText().isEmpty() || addParts_price.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields!");
        } else {
            try {
                ObservableList<J_parts_modify_get> listData = addPartsGetData();
                for (J_parts_modify_get p : listData) {
                    if (p.getName().equals(addParts_name.getText())) {
                        alert.errorMessage("A part named \"" + addParts_name.getText() + "\" already exists!");
                        return; //genlus solution
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (alert.confirmMessage("Are you sure you want to ADD the part: \"" + addParts_name.getText() + "\"?"))
            {
                communicateSend("/parts-new", gson.toJson(new J_parts_modify_get(addParts_name.getText(), Integer.parseInt(addParts_price.getText()), addParts_quantitySpnr.getValue())));
                System.out.println("Added the part: " + addParts_name.getText());
                addPartsDisplayData();
                alert.successMessage("Added new part successfully!");
                addPartsClearFields();
            }
        }
    }

    /**
     * This method handles the process of modifying a part.
     */
    public void addPartsModify() {
        if (partID == null) {
            alert.errorMessage("Please select a part from the list first!");
        } else if (addParts_name.getText().isEmpty() || addParts_price.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields!");
        } else if (addParts_name.getText().equals(Parts_tableView.getSelectionModel().getSelectedItem().getName()) && addParts_price.getText().equals(String.valueOf(Parts_tableView.getSelectionModel().getSelectedItem().getPrice())) && addParts_quantitySpnr.getValue().equals(Parts_tableView.getSelectionModel().getSelectedItem().getMaxnum())) {
            alert.errorMessage("No changes were made!");
        } else {
            if (alert.confirmMessage("Are you sure you want to MODIFY the part: \"" + Parts_tableView.getSelectionModel().getSelectedItem().getName() + "\"?")) {
                communicateSend("/parts-modify", gson.toJson(new J_parts_modify_get(partID, addParts_name.getText(), Integer.parseInt(addParts_price.getText()), addParts_quantitySpnr.getValue())));
                System.out.println("Modified the part: " + partID);
                addPartsDisplayData();
                alert.successMessage("Modified the part successfully!");
                addPartsClearFields();
            }
        }
    }

    /**
     * This method clears the fields for adding or modifying a part.
     */
    public void addPartsClearFields() {
        partID = null;
        Parts_tableView.getSelectionModel().clearSelection();
        addParts_name.clear();
        addParts_price.clear();
        addParts_quantitySpnr.getValueFactory().setValue(1);
    }

    /**
     * This method handles the click event on the parts pane in the dashboard.
     */
    public void onDashboard_TPartsPaneClick() {
        switchForm(new ActionEvent(getParts_btn, ActionEvent.NULL_SOURCE_TARGET));
    }

    /**
     * This method switches the form based on the source of the event.
     * @param event The action event.
     */
    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            parts_form.setVisible(false);
            dashboardDisplayTParts();
            dashboardPartsChart();

        } else if (event.getSource() == getParts_btn) {
            dashboard_form.setVisible(false);
            parts_form.setVisible(true);
            addPartsDisplayData();

        } else if (event.getSource() == logout_btn) {
            if (alert.confirmMessage("Are you sure you want to logout?")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Project Management System");
                    stage.setScene(new Scene(root));
                    stage.show();

                    logout_btn.getScene().getWindow().hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method displays a greeting message to the user.
     */
    public void displayGreet() {
        String username = J_parts_modify_get.username;
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        greet_username.setText("Welcome, " + username + "!");
    }

    /**
     * This method is used to display the role of the user.
     * It retrieves the role from the J_parts_modify_get class, capitalizes the first letter, and sets the text of the user_role label.
     */
    public void displayRole() {
        String role = J_parts_modify_get.role;
        role = role.substring(0, 1).toUpperCase() + role.substring(1);

        user_role.setText(role);
    }

    /**
     * This method is used to display the current date and time.
     * It creates a Timeline that updates every second, formats the current date and time, and sets the text of the date_time label.
     */
    public void runTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy. MM. dd.   HH:mm:ss");
            date_time.setText(format.format(new Date()));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * This method is called to initialize a controller after its root element has been completely processed.
     * It runs the runTime and displayGreet methods, displays the role of the user, and sets the visibility of various elements based on the user's role.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime();
        displayGreet();
        displayRole();
        switch (J_parts_modify_get.role) {
            case "admin":
                dashboard_btn.setVisible(false);
                getParts_btn.setVisible(false);
                addUser_btn.setVisible(true);
                dashboard_form.setVisible(false);
                addUser_form.setVisible(true);
                addUser_roleSelectorInitialize();
                break;
            case "manager":
                dashboard_form.setVisible(true);
                dashboardDisplayTParts();
                dashboardPartsChart();
                addPartsSpnrInitialize();
                addPartsPrice_NumOnly();
                break;
            case "professional", "operator":
                getParts_btn.setVisible(false);
                break;
            default:
                break;
        }
    }
}