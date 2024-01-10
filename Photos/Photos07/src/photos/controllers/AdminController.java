package photos.controllers;

import java.io.File;
import java.util.ArrayList;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import photos.Photo;
import photos.Photos;
import photos.Tag;
import photos.User;
import photos.Utility;

/**
 * Our controller class for the admin page.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class AdminController {
    /**
     * The field for the admin to type the username of a new user.
     */
    @FXML TextField user;

    /**
     * The button for the admin to add a new user.
     */
    @FXML Button add;

    /**
     * The button for the admin to delete a selected user.
     */
    @FXML Button delete;

    /**
     * The list of all existing users. List displays usernames.
     */
    @FXML ListView<String> list;

    /**
     * The button to log out of this admin page.
     */
    @FXML Button logout;
    
    private static ObservableList<String> obsList = FXCollections.observableArrayList();

    /**
     * The method to initialize this scene.
     */
    public void initialize() {
        Utility.adminStage.setOnCloseRequest(e -> {
            try {
                logout();
            } catch (Exception event) {}
        });
        obsList.clear();
        for (String u : Utility.userNames) {
            obsList.add(u);
        }
        list.setItems(obsList);
        list.getSelectionModel().selectedIndexProperty().addListener(e -> updateUI());
        updateUI();
    }

    /**
     * Enables or disables the delete user button.
     */
    public void updateUI() {
        delete.setDisable(list.getSelectionModel().getSelectedIndex() == -1);
    }

    /**
     * Adds a user to the system when the add button is pressed.
     */
    public void add() {
        if (Utility.userNames.contains(user.getText())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("User already exists.");
            alert.setContentText("You cannot add an existing user. Please add a new user.");
            alert.showAndWait();
        } else {
            Utility.userNames.add(user.getText());
            User newUser = new User(user.getText());
            Utility.users.add(newUser);
            Utility.photos.add(new ArrayList<Photo>());
            Utility.files.add(new ArrayList<File>());
            Utility.useCount.add(new ArrayList<Integer>());
            Utility.directories.add(new ArrayList<String>());
            obsList.add(user.getText());
            list.setItems(obsList);
            list.getSelectionModel().clearAndSelect(obsList.size()-1);
            user.setText("");
            newUser.tags.add(new Tag(true, null, false));
            newUser.tags.add(new Tag(true, "date", false));
            newUser.tags.add(new Tag(true, "location", false));
            newUser.tags.add(new Tag(false, "person", false));
        }
    }
    
    /**
     * Deletes a user from the system when the delete button is pressed.
     */
    public void delete() {
        int userIndex = list.getSelectionModel().getSelectedIndex();
        if (userIndex == -1) return;
        Utility.users.remove(userIndex);
        Utility.userNames.remove(Utility.userNames.get(userIndex));
        Utility.photos.remove(userIndex);
        Utility.files.remove(userIndex);
        Utility.useCount.remove(userIndex);
        Utility.directories.remove(userIndex);
        obsList.remove(userIndex);
        list.setItems(obsList);
    }

    /**
     * Performs a logout. Returns to login page and writes app data to data file.
     * @throws Exception if issues occur when switching scenes.
     */
    public void logout() throws Exception {
        Stage primaryStage = (Stage) logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setTitle("Login");
        Photos.writeApp();
    }
}