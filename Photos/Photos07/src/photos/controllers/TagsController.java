package photos.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import photos.Tag;
import photos.Utility;

/**
 * Our controller class for the tag manager window.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class TagsController {
    /**
     * A list of all tags for the user.
     */
    @FXML ListView<Tag> tagList;

    /**
     * The button to delete a selected tag.
     */
    @FXML Button delete;

    /**
     * The button to create a new tag.
     */
    @FXML Button create;

    /**
     * A field for the user to enter the name of a new tag.
     */
    @FXML TextField tagName;

    /**
     * A checkbox to indicate whether or not a new tag should be singular.
     */
    @FXML CheckBox isSingular;

    /**
     * The observable list of tags.
     */
    ObservableList<Tag> obsList;

    /**
     * The method to initialize this scene.
     */
    public void initialize() {
        obsList = FXCollections.observableArrayList(Utility.currentUser.tags);
        obsList.remove(new Tag(false, "Date"));
        tagList.setItems(obsList);
        tagList.getSelectionModel().selectedIndexProperty().addListener(e -> updateUI());
        tagName.textProperty().addListener(e -> updateUI());
        tagList.getSelectionModel().clearAndSelect(0);
        Utility.openedTagsWindows.add(this);
    }
    
    /**
     * Updates UI based on the state of the program.
     */
    public void updateUI() {
        boolean enable = tagList.getSelectionModel().getSelectedItem().removable;
        delete.setDisable(!enable);
        create.setDisable(tagName.getText().equals(""));
    }
    
    /**
     * Creates a new tag based on information given by the user.
     */
    public void createTag() {
        boolean singular = isSingular.isSelected();
        String name = tagName.getText();
        Tag tag = new Tag(singular, name);
        if (Utility.currentUser.tags.contains(tag)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Tag already exists.");
            alert.setContentText("You cannot add an existing tag. Please add a new tag.");
            alert.showAndWait();
        }
        else {
            Utility.currentUser.tags.add(tag);
            obsList.add(tag);
            tagList.setItems(obsList);
            tagName.setText("");
        }
    }

    /**
     * Deletes a selected tag.
     */
    public void deleteTag() { 
        if (tagList.getSelectionModel().getSelectedIndex() == -1) return;
        Tag t = tagList.getSelectionModel().getSelectedItem();
        if (!t.removable) return;
        obsList.remove(t);
        Utility.currentUser.tags.remove(t);
        tagList.setItems(obsList);
    }

    /**
     * Closes this stage.
     */
    public void close() {
        Stage tagsStage = (Stage) create.getScene().getWindow();
        tagsStage.close();
    }
}