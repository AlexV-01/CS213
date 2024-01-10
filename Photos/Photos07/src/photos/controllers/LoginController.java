package photos.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import photos.Utility;

/**
 * Our controller class for the login page.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class LoginController {
    /**
     * The field for the user to enter a username.
     */
    @FXML TextField username;

    /**
     * The button to perform a login.
     */
    @FXML Button login;

    /**
     * The method to initialize this scene.
     */
    public void initialize() {
    }

    /**
     * Performs a login for the account specified by the <code>username</code> field.
     * @throws Exception if issues occur during login
     */
    public void login() throws Exception {
        if (username.getText().equals("admin")) {
            Stage primaryStage = (Stage) login.getScene().getWindow();
            Utility.adminStage = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.setTitle("Admin");
        } else if (Utility.userNames.contains(username.getText())) {
            Utility.currentUser = Utility.getUserByName(username.getText());
            Stage primaryStage = (Stage) login.getScene().getWindow();
            Utility.currentUserStage = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("user.fxml"));
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.setTitle(username.getText());
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid username");
            alert.setContentText("This user does not exist. Please enter an existing username.");
            alert.showAndWait();
        }
    }
}